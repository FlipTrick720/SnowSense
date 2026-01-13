package com.notification.controller;

import com.notification.model.SkiResort;
import com.notification.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/recommendation")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    /**
     * Get best recommended ski resort based on temperature proximity to -4°C
     * GET /api/recommendation/skiresort
     */
    @GetMapping("/skiresort")
    public ResponseEntity<Map<String, Object>> recommendResort(
            @RequestParam(defaultValue = "-4") double targetTemp
    ) {
        SkiResort recommended = recommendationService.getBestResortBasedOnTemperature(targetTemp);

        Map<String, Object> response = new HashMap<>();

        if (recommended == null) {
            response.put("status", "error");
            response.put("message", "No weather data available for recommendation");
            return ResponseEntity.status(404).body(response);
        }

        response.put("status", "success");
        response.put("targetTemperature", targetTemp);
        response.put("recommendedResort", recommended);

        return ResponseEntity.ok(response);
    }
}
