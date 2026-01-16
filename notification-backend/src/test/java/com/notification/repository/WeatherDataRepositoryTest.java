package com.notification.repository;

import com.notification.model.SkiResort;
import com.notification.model.WeatherData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class WeatherDataRepositoryTest {

    @Autowired
    private SkiResortRepository skiResortRepository;

    @Autowired
    private WeatherDataRepository weatherDataRepository;

    @Test
    void findBySkiResortIdOrderByTimestampDesc_shouldReturnOrdered() {
        SkiResort resort = new SkiResort(null, "R", "b", 47.0, 11.0, 1000, null);
        resort = skiResortRepository.save(resort);

        WeatherData w1 = WeatherData.builder().skiResort(resort).timestamp(LocalDateTime.now().minusHours(2)).temperature(1.0).build();
        WeatherData w2 = WeatherData.builder().skiResort(resort).timestamp(LocalDateTime.now().minusHours(1)).temperature(2.0).build();
        weatherDataRepository.saveAll(List.of(w1, w2));

        List<WeatherData> result = weatherDataRepository.findBySkiResortIdOrderByTimestampDesc(resort.getId());
        assertEquals(2, result.size());
        assertTrue(result.get(0).getTimestamp().isAfter(result.get(1).getTimestamp()));

        assertEquals(2, weatherDataRepository.findByTimestampAfter(LocalDateTime.now().minusHours(3)).size());
    }
}
