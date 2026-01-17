package com.notification.service.impl;

import com.notification.service.ScrapingService;
import com.notification.service.AvalancheService;
import com.notification.service.WeatherService;
import com.notification.service.SkiResortService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ScrapingServiceImpl implements ScrapingService {

    private static final Logger logger = LoggerFactory.getLogger(ScrapingServiceImpl.class);
    
    private final AvalancheService avalancheService;
    private final WeatherService weatherService;
    private final SkiResortService skiResortService;

    public ScrapingServiceImpl(
        AvalancheService avalancheService,
        WeatherService weatherService,
        SkiResortService skiResortService
    ) {
        this.avalancheService = avalancheService;
        this.weatherService = weatherService;
        this.skiResortService = skiResortService;
    }

    /**
     * Fast startup scraping - Only avalanche and weather data
     * Used during application startup to minimize boot time
     * Ski resort infrastructure scraping is done manually via API
     * 
     * Duration: ~2 minutes (vs ~30 minutes for full scraping)
     * Allows Spring Boot to start and respond to health checks immediately
     */
    @Override
    public void triggerFastStartupScraping() {
        logger.info("========================================");
        logger.info("Starting FAST startup scraping (no ski resort data)...");
        logger.info("========================================");
        
        try {
            // 1. Scrape Avalanche Data First
            logger.info("Step 1: Scraping avalanche data...");
            avalancheService.scrapeAvalancheData();
            logger.info("Avalanche data scraping completed");
            
            // Small delay between operations
            Thread.sleep(1000);
            
            // 2. Scrape Weather Data
            logger.info("Step 2: Scraping weather data...");
            weatherService.scrapeWeatherForAllResorts();
            logger.info("Weather data scraping completed");
            
            logger.info("========================================");
            logger.info("Fast startup scraping completed successfully!");
            logger.info("Note: Ski resort infrastructure not included (manual via API)");
            logger.info("========================================");
        } catch (InterruptedException e) {
            logger.error("Fast startup scraping interrupted", e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.error("Error during fast startup scraping", e);
        }
    }

    /**
     * Full data scraping sequence - Includes ski resort infrastructure
     * Used for manual triggers via /api/scrape endpoint
     * 
     * Duration: ~30 minutes
     * Can be triggered manually after deployment
     */
    @Override
    public void triggerScraping() {
        logger.info("========================================");
        logger.info("Starting FULL data scraping sequence...");
        logger.info("========================================");
        
        try {
            // 1. Scrape Avalanche Data First
            logger.info("Step 1: Scraping avalanche data...");
            avalancheService.scrapeAvalancheData();
            logger.info("Avalanche data scraping completed");
            
            // Small delay between operations
            Thread.sleep(1000);
            
            // 2. Scrape Weather Data
            logger.info("Step 2: Scraping weather data...");
            weatherService.scrapeWeatherForAllResorts();
            logger.info("Weather data scraping completed");
            
            // Small delay between operations
            Thread.sleep(1000);
            
            // 3. Scrape Resort Infrastructure (Lifts & Slopes)
            logger.info("Step 3: Scraping ski resort infrastructure...");
            skiResortService.scrapeSkiResortStatusForAllResorts();
            logger.info("Ski resort infrastructure scraping completed");
            
            logger.info("All scraping operations completed successfully!");
        } catch (InterruptedException e) {
            logger.error("Scraping sequence interrupted", e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.error("Error during scraping sequence", e);
        }
    }

    @Override
    public void scheduleScrapingTask(Object config) {
        logger.info("Scheduling scraping task...");
        // Can be used for custom scheduled tasks in the future
    }
}