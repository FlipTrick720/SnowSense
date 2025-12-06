package com.notification.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Mapping between ski resorts and avalanche warning regions
 * One resort can be in multiple regions, one region can cover multiple resorts
 */
@Entity
@Table(name = "ski_resort_avalanche_region")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkiResortAvalancheRegion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ski_resort_id", nullable = false)
    private SkiResort skiResort;
    
    @Column(name = "region_code", nullable = false, length = 50)
    private String regionCode;
    
    @Column(name = "region_name", length = 255)
    private String regionName;
    
    @Column(name = "is_primary")
    private Boolean isPrimary;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (isPrimary == null) {
            isPrimary = true;
        }
    }
}
