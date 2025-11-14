package com.notification.controller;

import com.notification.model.AvalancheData;
import com.notification.service.AvalancheService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/avalanche")
public class AvalancheController {
    
    private final AvalancheService avalancheService;
    
    public AvalancheController(AvalancheService avalancheService) {
        this.avalancheService = avalancheService;
    }
    
    /**
     * Manually trigger avalanche data scrape
     */
    @PostMapping("/scrape")
    public ResponseEntity<String> triggerScrape() {
        avalancheService.manualScrape();
        return ResponseEntity.ok("Avalanche data scrape triggered");
    }
    
    /**
     * Get all avalanche bulletins
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
