package com.notification.service;

import com.notification.model.SkiResort;
import com.notification.model.WeatherData;
import com.notification.repository.WeatherDataRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class RecommendationService {

    private final WeatherDataRepository weatherDataRepository;

    public RecommendationService(WeatherDataRepository weatherDataRepository) {
        this.weatherDataRepository = weatherDataRepository;
    }

    public SkiResort getBestResortBasedOnTemperature(double targetTemperature) {
        List<WeatherData> latestWeather = weatherDataRepository.findLatestWeatherPerResort();

        if (latestWeather.isEmpty()) {
            return null;
        }

        WeatherData bestMatch = latestWeather.stream()
                .filter(w -> w.getTemperature() != null)
                .min(Comparator.comparingDouble(w -> Math.abs(w.getTemperature() - targetTemperature)))
                .orElse(null);

        return bestMatch != null ? bestMatch.getSkiResort() : null;
    }
}
