package com.notification.repository;

import com.notification.model.SkiResortAvalancheRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkiResortAvalancheRegionRepository extends JpaRepository<SkiResortAvalancheRegion, Long> {
    
    /**
     * Find all mappings for a specific ski resort
     */
    List<SkiResortAvalancheRegion> findBySkiResortId(Long skiResortId);
    
    /**
     * Find all ski resorts in a specific avalanche region
     */
    List<SkiResortAvalancheRegion> findByRegionCode(String regionCode);
    
    /**
     * Find primary region for a ski resort
     */
    @Query("SELECT r FROM SkiResortAvalancheRegion r WHERE r.skiResort.id = :resortId AND r.isPrimary = true")
    SkiResortAvalancheRegion findPrimaryRegionForResort(@Param("resortId") Long resortId);
    
    /**
     * Find all ski resorts affected by a list of region codes
     */
    @Query("SELECT DISTINCT r FROM SkiResortAvalancheRegion r WHERE r.regionCode IN :regionCodes")
    List<SkiResortAvalancheRegion> findByRegionCodeIn(@Param("regionCodes") List<String> regionCodes);
}
