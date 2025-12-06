package com.notification.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    
    // TODO: Inject services when logic is implemented
    // private final ConditionsService conditionsService;
    
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
     * Trigger scraping of both weather and avalanche data
     * POST /api/conditions/scrape
     * 
     */
    @PostMapping("/scrape")
    public ResponseEntity<Map<String, String>> triggerScrape() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "not_implemented");
        response.put("message", "Combined scrape trigger - logic to be implemented");
        response.put("description", "Will trigger both weather and avalanche scraping");
        response.put("note", "Avalanche: daily at 8 AM, Weather: every 15 min");
        return ResponseEntity.ok(response);
    }
}
