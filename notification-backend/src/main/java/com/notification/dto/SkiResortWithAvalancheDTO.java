package com.notification.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Combined DTO with ski resort and avalanche data
 * For recommendation system and frontend display
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkiResortWithAvalancheDTO {
    
    // Ski resort info
    private Long resortId;
    private String resortName;
    private Double latitude;
    private Double longitude;
    private Integer elevation;
    
    // Avalanche region mapping
    private String avalancheRegionCode;
    private String avalancheRegionName;
    
    // Current avalanche data
    private String dangerLevel;
    private String dangerLevelAfternoon;
    private String problemTypes;
    private String aspects;
    private String elevationLower;
    private String elevationUpper;
    private String highlights;
    private String tendencyType;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;
    
    // Safety assessment
    private String safetyStatus; // SAFE, CAUTION, WARNING, DANGER, UNKNOWN
    private String recommendation;
}
