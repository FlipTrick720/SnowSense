package com.notification.controller;

import com.notification.dto.SkiResortWithAvalancheDTO;
import com.notification.service.SkiResortAvalancheService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for combined ski resort and avalanche data
 * Main endpoint for recommendation system
 */
@RestController
@RequestMapping("/api/resorts")
public class SkiResortAvalancheController {
    
    private final SkiResortAvalancheService service;
    
    public SkiResortAvalancheController(SkiResortAvalancheService service) {
        this.service = service;
    }
    
    /**
     * Get all ski resorts with current avalanche data
     * Main endpoint for recommendation system
     */
    @GetMapping("/with-avalanche")
    public ResponseEntity<List<SkiResortWithAvalancheDTO>> getAllResortsWithAvalanche() {
        return ResponseEntity.ok(service.getAllResortsWithAvalancheData());
    }
    
    /**
     * Get single resort with avalanche data
     */
    @GetMapping("/{id}/with-avalanche")
    public ResponseEntity<SkiResortWithAvalancheDTO> getResortWithAvalanche(@PathVariable Long id) {
        return ResponseEntity.ok(service.getResortWithAvalancheData(id));
    }
    
    /**
     * Get only safe resorts (low/moderate danger)
     */
    @GetMapping("/safe")
    public ResponseEntity<List<SkiResortWithAvalancheDTO>> getSafeResorts() {
        return ResponseEntity.ok(service.getSafeResorts());
    }
    
    /**
     * Get resorts with high danger warnings
     */
    @GetMapping("/high-danger")
    public ResponseEntity<List<SkiResortWithAvalancheDTO>> getHighDangerResorts() {
        return ResponseEntity.ok(service.getHighDangerResorts());
    }
}
