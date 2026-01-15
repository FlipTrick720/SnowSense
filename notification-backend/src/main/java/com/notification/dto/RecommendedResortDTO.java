package com.notification.dto;

import com.notification.model.SkiResort;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecommendedResortDTO {
    private SkiResort resort;
    private double distanceKm;
    private double penaltyScore;
}
