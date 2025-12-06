package com.notification.service;

import com.notification.dto.SkiResortWithAvalancheDTO;
import com.notification.model.AvalancheData;
import com.notification.model.SkiResort;
import com.notification.model.SkiResortAvalancheRegion;
import com.notification.repository.AvalancheDataRepository;
import com.notification.repository.SkiResortAvalancheRegionRepository;
import com.notification.repository.SkiResortRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Service to combine ski resort and avalanche data
 */
@Service
public class SkiResortAvalancheService {
    
    private final SkiResortRepository skiResortRepository;
    private final AvalancheDataRepository avalancheDataRepository;
    private final SkiResortAvalancheRegionRepository regionMappingRepository;
    
    public SkiResortAvalancheService(
            SkiResortRepository skiResortRepository,
            AvalancheDataRepository avalancheDataRepository,
            SkiResortAvalancheRegionRepository regionMappingRepository) {
        this.skiResortRepository = skiResortRepository;
        this.avalancheDataRepository = avalancheDataRepository;
        this.regionMappingRepository = regionMappingRepository;
    }
    
    /**
     * Get all ski resorts with their current avalanche data
     */
    public List<SkiResortWithAvalancheDTO> getAllResortsWithAvalancheData() {
        List<SkiResort> resorts = skiResortRepository.findAll();
        List<SkiResortWithAvalancheDTO> result = new ArrayList<>();
        
        for (SkiResort resort : resorts) {
            result.add(getResortWithAvalancheData(resort.getId()));
        }
        
        return result;
    }

    /**
     * Get a single ski resort with avalanche data
     */
    public SkiResortWithAvalancheDTO getResortWithAvalancheData(Long resortId) {
        SkiResort resort = skiResortRepository.findById(resortId)
                .orElseThrow(() -> new RuntimeException("Resort not found: " + resortId));
        
        SkiResortWithAvalancheDTO.SkiResortWithAvalancheDTOBuilder builder = SkiResortWithAvalancheDTO.builder()
                .resortId(resort.getId())
                .resortName(resort.getName())
                .latitude(resort.getLatitude())
                .longitude(resort.getLongitude())
                .elevation(resort.getElevation());
        
        // Find avalanche region mapping
        SkiResortAvalancheRegion regionMapping = regionMappingRepository.findPrimaryRegionForResort(resortId);
        
        if (regionMapping != null) {
            builder.avalancheRegionCode(regionMapping.getRegionCode())
                   .avalancheRegionName(regionMapping.getRegionName());
            
            // Find current avalanche data for this region
            AvalancheData avalancheData = findCurrentAvalancheDataForRegion(regionMapping.getRegionCode());
            
            if (avalancheData != null) {
                builder.dangerLevel(avalancheData.getDangerLevel())
                       .dangerLevelAfternoon(avalancheData.getDangerLevelAfternoon())
                       .problemTypes(avalancheData.getProblemTypes())
                       .aspects(avalancheData.getAspects())
                       .elevationLower(avalancheData.getElevationLower())
                       .elevationUpper(avalancheData.getElevationUpper())
                       .highlights(avalancheData.getHighlights())
                       .tendencyType(avalancheData.getTendencyType())
                       .validFrom(avalancheData.getValidTimeStart())
                       .validUntil(avalancheData.getValidTimeEnd());
                
                // Calculate safety status
                String[] safety = calculateSafetyStatus(avalancheData.getDangerLevel());
                builder.safetyStatus(safety[0])
                       .recommendation(safety[1]);
            } else {
                builder.safetyStatus("UNKNOWN")
                       .recommendation("No current avalanche data available");
            }
        } else {
            builder.safetyStatus("UNKNOWN")
                   .recommendation("No avalanche region mapping available");
        }
        
        return builder.build();
    }
    
    /**
     * Get only safe resorts (danger level low or moderate)
     */
    public List<SkiResortWithAvalancheDTO> getSafeResorts() {
        return getAllResortsWithAvalancheData().stream()
                .filter(dto -> "SAFE".equals(dto.getSafetyStatus()))
                .toList();
    }
    
    /**
     * Get resorts with high danger
     */
    public List<SkiResortWithAvalancheDTO> getHighDangerResorts() {
        return getAllResortsWithAvalancheData().stream()
                .filter(dto -> "WARNING".equals(dto.getSafetyStatus()) || 
                              "DANGER".equals(dto.getSafetyStatus()))
                .toList();
    }

    /**
     * Find current avalanche data for a specific region
     */
    private AvalancheData findCurrentAvalancheDataForRegion(String regionCode) {
        LocalDateTime now = LocalDateTime.now();
        
        // Get all currently valid bulletins
        List<AvalancheData> currentBulletins = avalancheDataRepository.findCurrentlyValid(now);
        
        // Find bulletin that covers this region
        for (AvalancheData bulletin : currentBulletins) {
            if (bulletin.getRegionCodes() != null && 
                bulletin.getRegionCodes().contains(regionCode)) {
                return bulletin;
            }
        }
        
        return null;
    }
    
    /**
     * Calculate safety status and recommendation based on danger level
     */
    private String[] calculateSafetyStatus(String dangerLevel) {
        if (dangerLevel == null) {
            return new String[]{"UNKNOWN", "No danger level information available"};
        }
        
        return switch (dangerLevel.toLowerCase()) {
            case "low" -> new String[]{
                "SAFE",
                "Low avalanche danger. Generally safe conditions for all activities."
            };
            case "moderate" -> new String[]{
                "SAFE",
                "Moderate danger. Safe on groomed runs. Exercise caution in off-piste areas."
            };
            case "considerable" -> new String[]{
                "CAUTION",
                "Considerable danger. Stick to marked runs. Avoid off-piste skiing."
            };
            case "high" -> new String[]{
                "WARNING",
                "High danger. Stay on groomed runs only. Off-piste skiing not recommended."
            };
            case "very_high" -> new String[]{
                "DANGER",
                "Very high danger. Extreme avalanche conditions. Consider alternative activities."
            };
            default -> new String[]{
                "UNKNOWN",
                "Unknown danger level: " + dangerLevel
            };
        };
    }
}
