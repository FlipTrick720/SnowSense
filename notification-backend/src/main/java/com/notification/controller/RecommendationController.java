package com.notification.controller;

import com.notification.dto.RecommendationRequest;
import com.notification.dto.RecommendedResortDTO;
import com.notification.service.RecommendationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recommendation")
public class RecommendationController {

    private static final Logger logger = LoggerFactory.getLogger(RecommendationController.class);
    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    /**
     * Recommend ski resorts based on user location & conditions
     * Supports both POST (with JSON body) and GET (with query parameters)
     * 
     * POST /api/recommendation/skiresort
     * Body: { "latitude": 47.2, "longitude": 12.5 }
     * 
     * GET /api/recommendation/skiresort?latitude=47.2&longitude=12.5
     */
    @PostMapping("/skiresort")
    public ResponseEntity<Map<String, Object>> recommendResortsPost(
            @RequestBody RecommendationRequest request
    ) {
        return getRecommendations(request.getLatitude(), request.getLongitude());
    }

    @GetMapping("/skiresort")
    public ResponseEntity<Map<String, Object>> recommendResortsGet(
            @RequestParam double latitude,
            @RequestParam double longitude
    ) {
        logger.info("GET /api/recommendation/skiresort called with lat={}, lon={}", latitude, longitude);
        return getRecommendations(latitude, longitude);
    }

    private ResponseEntity<Map<String, Object>> getRecommendations(double latitude, double longitude) {
        try {
            List<RecommendedResortDTO> results =
                    recommendationService.recommendResorts(latitude, longitude);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("count", results.size());
            response.put("recommendations", results);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error in getRecommendations: ", e);
            throw e;
        }
    }
}
