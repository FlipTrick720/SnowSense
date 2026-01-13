package com.notification.repository;

import com.notification.model.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {

    List<WeatherData> findBySkiResortIdOrderByTimestampDesc(Long skiResortId);

    List<WeatherData> findByTimestampAfter(LocalDateTime timestamp);

    /**
     * Get the latest weather entry for each ski resort.
     * Uses MAX(id) because your weather rows are created sequentially.
     */
    @Query("""
        SELECT wd FROM WeatherData wd
        WHERE wd.id IN (
            SELECT MAX(wd2.id)
            FROM WeatherData wd2
            GROUP BY wd2.skiResort.id
        )
        """)
    List<WeatherData> findLatestWeatherPerResort();
}
