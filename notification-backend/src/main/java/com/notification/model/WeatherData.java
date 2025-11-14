package com.notification.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "weather_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherData {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ski_resort_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private SkiResort skiResort;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    private Double temperature;
    
    @Column(name = "wind_speed")
    private Double windSpeed;
    
    @Column(name = "wind_direction")
    private Integer windDirection;
    
    private Double precipitation;
    
    private Double snowfall;
    
    @Column(name = "snow_depth")
    private Double snowDepth;
    
    @Column(name = "cloud_cover")
    private Integer cloudCover;
    
    private Double visibility;
    
    @Column(name = "freezing_level")
    private Integer freezingLevel;
    
    @Column(name = "weather_code")
    private Integer weatherCode;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
