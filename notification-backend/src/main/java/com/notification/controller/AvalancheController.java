package com.notification.controller;

import com.notification.model.AvalancheData;
import com.notification.service.AvalancheService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/avalanche")
public class AvalancheController {
    
    private final AvalancheService avalancheService;
    
    public AvalancheController(AvalancheService avalancheService) {
        this.avalancheService = avalancheService;
    }
    
    /**
     * Manually trigger avalanche data scrape
     * POST /api/avalanche/scrape
     * 
     * Note: Avalanche data is automatically scraped daily at 8:00 AM
     */
    @PostMapping("/scrape")
    public ResponseEntity<Map<String, String>> triggerScrape() {
        avalancheService.manualScrape();
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Avalanche data scrape triggered successfully");
        response.put("note", "Automatic scraping runs daily at 8:00 AM");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get all avalanche bulletins
     * GET /api/avalanche
     */
    @GetMapping
    public ResponseEntity<List<AvalancheData>> getAllBulletins() {
        return ResponseEntity.ok(avalancheService.getAllAvalancheData());
    }
    
    /**
     * Get currently valid bulletins
     */
    @GetMapping("/current")
    public ResponseEntity<List<AvalancheData>> getCurrentBulletins() {
        return ResponseEntity.ok(avalancheService.getCurrentBulletins());
    }
    
    /**
     * Get bulletins for specific region
     */
    @GetMapping("/region/{regionCode}")
    public ResponseEntity<List<AvalancheData>> getBulletinsForRegion(@PathVariable String regionCode) {
        return ResponseEntity.ok(avalancheService.getBulletinsForRegion(regionCode));
    }
    
    /**
     * Get high danger bulletins
     */
    @GetMapping("/high-danger")
    public ResponseEntity<List<AvalancheData>> getHighDangerBulletins() {
        return ResponseEntity.ok(avalancheService.getHighDangerBulletins());
    }
}
