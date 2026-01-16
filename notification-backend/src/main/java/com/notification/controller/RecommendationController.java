package com.notification.controller;

import com.notification.dto.RecommendationRequest;
import com.notification.dto.RecommendedResortDTO;
import com.notification.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recommendation")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    /**
     * Recommend ski resorts based on user location & conditions
     * POST /api/recommendation/skiresort
     */
    @PostMapping("/skiresort")
    public ResponseEntity<Map<String, Object>> recommendResorts(
            @RequestBody RecommendationRequest request
    ) {
        List<RecommendedResortDTO> results =
                recommendationService.recommendResorts(
                        request.getLatitude(),
                        request.getLongitude()
                );

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("count", results.size());
        response.put("recommendations", results);

        return ResponseEntity.ok(response);
    }
}
