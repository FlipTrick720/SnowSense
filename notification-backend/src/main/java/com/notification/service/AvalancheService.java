package com.notification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notification.dto.CAAMLResponse;
import com.notification.model.AvalancheData;
import com.notification.repository.AvalancheDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvalancheService {
    
    private static final Logger logger = LoggerFactory.getLogger(AvalancheService.class);
    private static final String AVALANCHE_API = "https://static.avalanche.report/bulletins/latest/EUREGIO_de_CAAMLv6.json";
    
    private final AvalancheDataRepository avalancheDataRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    public AvalancheService(AvalancheDataRepository avalancheDataRepository) {
        this.avalancheDataRepository = avalancheDataRepository;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.findAndRegisterModules(); // For Java 8 date/time support
    }
    
    /**
     * Scrape avalanche data
     * Scheduled to run every day at 8:00 AM
     */
    @Scheduled(cron = "0 0 8 * * *")  // Every day at 8:00 AM
    public void scrapeAvalancheData() {
        logger.info("Starting avalanche data scrape");
        
        try {
            CAAMLResponse response = restTemplate.getForObject(AVALANCHE_API, CAAMLResponse.class);
            
            if (response != null && response.getBulletins() != null) {
                logger.info("Fetched {} avalanche bulletins", response.getBulletins().size());
                
                for (CAAMLResponse.Bulletin bulletin : response.getBulletins()) {
                    try {
                        scrapeAvalancheBulletin(bulletin);
                    } catch (Exception e) {
                        logger.error("Failed to process bulletin {}: {}", 
                                   bulletin.getBulletinID(), e.getMessage());
                    }
                }
                
                logger.info("Avalanche scrape completed successfully");
            } else {
                logger.warn("No bulletins received from API");
            }
        } catch (Exception e) {
            logger.error("Failed to scrape avalanche data: {}", e.getMessage(), e);
        }
    }

    /**
     * Process and store a single avalanche bulletin
     */
    private void scrapeAvalancheBulletin(CAAMLResponse.Bulletin bulletin) {
        logger.info("Processing bulletin: {}", bulletin.getBulletinID());
        
        AvalancheData avalancheData = mapToAvalancheData(bulletin);
        
        // Check if bulletin already exists (update if it does)
        avalancheDataRepository.findByBulletinId(bulletin.getBulletinID())
                .ifPresentOrElse(
                        existing -> {
                            avalancheData.setId(existing.getId());
                            avalancheData.setCreatedAt(existing.getCreatedAt());
                            avalancheDataRepository.save(avalancheData);
                            logger.info("Updated existing bulletin: {}", bulletin.getBulletinID());
                        },
                        () -> {
                            avalancheDataRepository.save(avalancheData);
                            logger.info("Saved new bulletin: {}", bulletin.getBulletinID());
                        }
                );
        
        printAvalancheData(avalancheData);
    }
    
    /**
     * Map CAAML bulletin to AvalancheData entity
     */
    private AvalancheData mapToAvalancheData(CAAMLResponse.Bulletin bulletin) {
        AvalancheData.AvalancheDataBuilder builder = AvalancheData.builder()
                .bulletinId(bulletin.getBulletinID())
                .publicationTime(bulletin.getPublicationTime())
                .unscheduled(bulletin.getUnscheduled())
                .highlights(bulletin.getHighlights())
                .comment(bulletin.getComment());
        
        // Valid time
        if (bulletin.getValidTime() != null) {
            builder.validTimeStart(bulletin.getValidTime().getStartTime())
                   .validTimeEnd(bulletin.getValidTime().getEndTime());
        }
        
        // Regions
        if (bulletin.getRegions() != null && !bulletin.getRegions().isEmpty()) {
            String regionCodes = bulletin.getRegions().stream()
                    .map(CAAMLResponse.Region::getRegionID)
                    .collect(Collectors.joining(","));
            builder.regionCodes(regionCodes);
        }
        
        // Danger ratings
        if (bulletin.getDangerRatings() != null && !bulletin.getDangerRatings().isEmpty()) {
            extractDangerRatings(bulletin.getDangerRatings(), builder);
        }
        
        // Avalanche problems
        if (bulletin.getAvalancheProblems() != null && !bulletin.getAvalancheProblems().isEmpty()) {
            extractAvalancheProblems(bulletin.getAvalancheProblems(), builder);
        }
        
        // Text content
        if (bulletin.getAvalancheActivity() != null) {
            builder.avalancheActivity(formatTextContent(bulletin.getAvalancheActivity()));
        }
        if (bulletin.getSnowpackStructure() != null) {
            builder.snowpackStructure(formatTextContent(bulletin.getSnowpackStructure()));
        }
        if (bulletin.getTravelAdvisory() != null) {
            builder.travelAdvisory(formatTextContent(bulletin.getTravelAdvisory()));
        }
        if (bulletin.getWxSynopsis() != null) {
            builder.wxSynopsis(formatTextContent(bulletin.getWxSynopsis()));
        }
        
        // Tendency
        if (bulletin.getTendency() != null && !bulletin.getTendency().isEmpty()) {
            builder.tendencyType(bulletin.getTendency().get(0).getTendencyType());
        }
        
        // Store raw JSON
        try {
            builder.rawData(objectMapper.writeValueAsString(bulletin));
        } catch (Exception e) {
            logger.warn("Failed to serialize bulletin to JSON: {}", e.getMessage());
        }
        
        return builder.build();
    }

    private void extractDangerRatings(List<CAAMLResponse.DangerRating> ratings, 
                                      AvalancheData.AvalancheDataBuilder builder) {
        // Get main danger level (usually first rating or "all_day")
        CAAMLResponse.DangerRating mainRating = ratings.stream()
                .filter(r -> r.getValidTimePeriod() == null || 
                           "all_day".equals(r.getValidTimePeriod()) ||
                           "earlier".equals(r.getValidTimePeriod()))
                .findFirst()
                .orElse(ratings.get(0));
        
        builder.dangerLevel(mainRating.getMainValue());
        
        if (mainRating.getElevation() != null) {
            builder.elevationLower(mainRating.getElevation().getLowerBound())
                   .elevationUpper(mainRating.getElevation().getUpperBound());
        }
        
        if (mainRating.getAspects() != null && !mainRating.getAspects().isEmpty()) {
            builder.aspects(String.join(",", mainRating.getAspects()));
        }
        
        // Check for afternoon rating
        ratings.stream()
                .filter(r -> "later".equals(r.getValidTimePeriod()))
                .findFirst()
                .ifPresent(afternoonRating -> 
                    builder.dangerLevelAfternoon(afternoonRating.getMainValue())
                );
    }
    
    private void extractAvalancheProblems(List<CAAMLResponse.AvalancheProblem> problems,
                                         AvalancheData.AvalancheDataBuilder builder) {
        List<String> problemTypes = problems.stream()
                .map(CAAMLResponse.AvalancheProblem::getProblemType)
                .filter(type -> type != null)
                .collect(Collectors.toList());
        
        if (!problemTypes.isEmpty()) {
            builder.problemTypes(String.join(",", problemTypes));
        }
    }
    
    private String formatTextContent(CAAMLResponse.TextContent content) {
        StringBuilder sb = new StringBuilder();
        if (content.getHighlights() != null) {
            sb.append(content.getHighlights());
        }
        if (content.getComment() != null) {
            if (sb.length() > 0) sb.append(" | ");
            sb.append(content.getComment());
        }
        return sb.toString();
    }

    /**
     * Print avalanche data to terminal (similar to weather service)
     */
    private void printAvalancheData(AvalancheData data) {
        logger.info("========================================");
        logger.info("Avalanche Bulletin: {}", data.getBulletinId());
        logger.info("========================================");
        logger.info("Publication Time: {}", data.getPublicationTime());
        logger.info("Valid: {} to {}", data.getValidTimeStart(), data.getValidTimeEnd());
        logger.info("Regions: {}", data.getRegionCodes());
        logger.info("Danger Level: {}", data.getDangerLevel());
        if (data.getDangerLevelAfternoon() != null) {
            logger.info("Danger Level (Afternoon): {}", data.getDangerLevelAfternoon());
        }
        logger.info("Elevation: {} - {}", data.getElevationLower(), data.getElevationUpper());
        logger.info("Aspects: {}", data.getAspects());
        logger.info("Problem Types: {}", data.getProblemTypes());
        logger.info("Tendency: {}", data.getTendencyType());
        logger.info("Highlights: {}", data.getHighlights());
        logger.info("========================================\n");
    }
    
    /**
     * Get all avalanche data from database
     */
    public List<AvalancheData> getAllAvalancheData() {
        return avalancheDataRepository.findAll();
    }
    
    /**
     * Get currently valid bulletins
     */
    public List<AvalancheData> getCurrentBulletins() {
        return avalancheDataRepository.findCurrentlyValid(LocalDateTime.now());
    }
    
    /**
     * Get bulletins for a specific region
     */
    public List<AvalancheData> getBulletinsForRegion(String regionCode) {
        return avalancheDataRepository.findByRegionCode(regionCode);
    }
    
    /**
     * Get high danger bulletins
     */
    public List<AvalancheData> getHighDangerBulletins() {
        return avalancheDataRepository.findHighDangerBulletins(LocalDateTime.now());
    }
    
    /**
     * Manual trigger for scraping (for testing)
     */
    public void manualScrape() {
        logger.info("Manual avalanche scrape triggered");
        scrapeAvalancheData();
    }
}
