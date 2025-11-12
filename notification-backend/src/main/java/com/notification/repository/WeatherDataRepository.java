package com.notification.repository;

import com.notification.model.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {
    List<WeatherData> findBySkiResortIdOrderByTimestampDesc(Long skiResortId);
    List<WeatherData> findByTimestampAfter(LocalDateTime timestamp);
}
