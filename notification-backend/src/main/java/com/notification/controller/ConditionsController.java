package com.notification.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.notification.service.ScrapingService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API for combined conditions data (weather + avalanche)
 * This endpoint will combine weather and avalanche data for ski resorts
 */
@RestController
@RequestMapping("/api/conditions")
public class ConditionsController {
    
    private final ScrapingService scrapingService;
    
    public ConditionsController(ScrapingService scrapingService) {
        this.scrapingService = scrapingService;
    }
    
    /**
     * Get combined weather and avalanche conditions for all resorts
     * GET /api/conditions
     * 
     * Returns: List of resorts with their latest weather and avalanche data
     */
    @GetMapping
    public ResponseEntity<Map<String, String>> getAllConditions() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "not_implemented");
        response.put("message", "Combined conditions endpoint - logic to be implemented");
        response.put("description", "Will return all ski resorts with their weather and avalanche data");
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get combined weather and avalanche conditions for a specific resort
     * GET /api/conditions/{resortId}
     * 
     * Returns: Resort with its latest weather and avalanche data
     */
    @GetMapping("/{resortId}")
    public ResponseEntity<Map<String, String>> getConditionsForResort(@PathVariable Long resortId) {
        Map<String, String> response = new HashMap<>();
        response.put("status", "not_implemented");
        response.put("message", "Combined conditions endpoint for resort " + resortId);
        response.put("description", "Will return specific resort with weather and avalanche data");
        return ResponseEntity.ok(response);
    }
    
    /**
     * Trigger FULL scraping including ski resort infrastructure
     * POST /api/conditions/scrape
     * 
     * This endpoint triggers a complete scraping of:
     * - Avalanche data
     * - Weather data for all resorts
     * - Ski resort infrastructure (lifts and slopes)
     * 
     * Warning: This can take 25-30 minutes to complete
     * Use after deployment to populate ski resort data
     */
    @PostMapping("/scrape")
    public ResponseEntity<Map<String, String>> triggerScrape() {
        // Run full scraping asynchronously so endpoint responds immediately
        new Thread(() -> {
            scrapingService.triggerScraping();
        }).start();
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "started");
        response.put("message", "Full scraping sequence initiated in background");
        response.put("duration", "25-30 minutes (approximately)");
        response.put("includes", "Avalanche data, Weather data, Ski resort infrastructure");
        response.put("note", "Server continues to function normally while scraping runs");
        return ResponseEntity.accepted().body(response);
    }
}
