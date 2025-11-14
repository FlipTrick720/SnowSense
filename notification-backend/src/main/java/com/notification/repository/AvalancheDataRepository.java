package com.notification.repository;

import com.notification.model.AvalancheData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvalancheDataRepository extends JpaRepository<AvalancheData, Long> {
    
    Optional<AvalancheData> findByBulletinId(String bulletinId);
    
    List<AvalancheData> findByPublicationTimeAfter(LocalDateTime timestamp);
    
    @Query("SELECT a FROM AvalancheData a WHERE a.validTimeStart <= :now AND a.validTimeEnd >= :now")
    List<AvalancheData> findCurrentlyValid(@Param("now") LocalDateTime now);
    
    @Query("SELECT a FROM AvalancheData a WHERE a.regionCodes LIKE %:regionCode%")
    List<AvalancheData> findByRegionCode(@Param("regionCode") String regionCode);
    
    @Query("SELECT a FROM AvalancheData a WHERE a.dangerLevel IN ('high', 'very_high') AND a.validTimeStart <= :now AND a.validTimeEnd >= :now")
    List<AvalancheData> findHighDangerBulletins(@Param("now") LocalDateTime now);
}
