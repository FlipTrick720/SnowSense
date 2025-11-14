package com.notification.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Entity for storing avalanche bulletin data
 * Similar pattern to WeatherData
 */
@Entity
@Table(name = "avalanche_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvalancheData {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "bulletin_id", unique = true, nullable = false)
    private String bulletinId;
    
    @Column(name = "publication_time", nullable = false)
    private LocalDateTime publicationTime;
    
    @Column(name = "valid_time_start", nullable = false)
    private LocalDateTime validTimeStart;
    
    @Column(name = "valid_time_end", nullable = false)
    private LocalDateTime validTimeEnd;
    
    @Column(name = "unscheduled")
    private Boolean unscheduled;
    
    // Region codes as comma-separated string (for easy querying)
    @Column(name = "region_codes", length = 1000)
    private String regionCodes;
    
    // Main danger rating (simplified for quick access)
    @Column(name = "danger_level")
    private String dangerLevel; // low, moderate, considerable, high, very_high
    
    @Column(name = "danger_level_afternoon")
    private String dangerLevelAfternoon;
    
    // Elevation info
    @Column(name = "elevation_lower")
    private String elevationLower;
    
    @Column(name = "elevation_upper")
    private String elevationUpper;
    
    // Aspects as comma-separated (N,NE,E,SE,S,SW,W,NW)
    @Column(name = "aspects", length = 100)
    private String aspects;

    // Problem types as comma-separated
    @Column(name = "problem_types", length = 200)
    private String problemTypes;
    
    // Text content
    @Column(name = "highlights", length = 2000)
    private String highlights;
    
    @Column(name = "comment", length = 5000)
    private String comment;
    
    @Column(name = "avalanche_activity", length = 5000)
    private String avalancheActivity;
    
    @Column(name = "snowpack_structure", length = 5000)
    private String snowpackStructure;
    
    @Column(name = "travel_advisory", length = 5000)
    private String travelAdvisory;
    
    @Column(name = "tendency_type")
    private String tendencyType; // steady, increasing, decreasing
    
    @Column(name = "wx_synopsis", length = 2000)
    private String wxSynopsis;
    
    // Store complete JSON for detailed analysis
    @Column(name = "raw_data", columnDefinition = "TEXT")
    private String rawData;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
