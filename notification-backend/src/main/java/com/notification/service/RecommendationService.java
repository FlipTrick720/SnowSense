package com.notification.service;

import com.notification.dto.RecommendedResortDTO;
import com.notification.model.AvalancheData;
import com.notification.model.SkiResort;
import com.notification.model.WeatherData;
import com.notification.repository.AvalancheDataRepository;
import com.notification.repository.SkiResortRepository;
import com.notification.repository.WeatherDataRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final SkiResortRepository skiResortRepository;
    private final WeatherDataRepository weatherDataRepository;
    private final AvalancheDataRepository avalancheDataRepository;

    public RecommendationService(
            SkiResortRepository skiResortRepository,
            WeatherDataRepository weatherDataRepository,
            AvalancheDataRepository avalancheDataRepository
    ) {
        this.skiResortRepository = skiResortRepository;
        this.weatherDataRepository = weatherDataRepository;
        this.avalancheDataRepository = avalancheDataRepository;
    }

    /* ---------------- IDEAL / THRESHOLD VALUES ---------------- */

    private static final double MIN_GOOD_SNOW = 0.30;      // 30 cm
    private static final double MAX_GOOD_SNOW = 1.50;      // 1.5 m
    private static final double WIND_THRESHOLD = 15.0;     // km/h
    private static final double IDEAL_TEMPERATURE = -4.0;  // °C

    /* ---------------- PUBLIC API ---------------- */

    public List<RecommendedResortDTO> recommendResorts(double userLat, double userLon) {

        // 1️⃣ Closest 20 resorts
        List<SkiResort> closestResorts = skiResortRepository.findAll().stream()
                .map(r -> Map.entry(r, distanceKm(userLat, userLon, r.getLatitude(), r.getLongitude())))
                .sorted(Map.Entry.comparingByValue())
                .limit(20)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // 2️⃣ Avalanche filtering
        List<AvalancheData> dangerousBulletins =
                avalancheDataRepository.findCurrentlyValid(LocalDateTime.now());

        Set<String> dangerousRegions = dangerousBulletins.stream()
                .filter(a -> isDangerous(a.getDangerLevel()))
                .flatMap(a -> Arrays.stream(a.getRegionCodes().split(",")))
                .collect(Collectors.toSet());

        List<WeatherData> latestWeather =
                weatherDataRepository.findLatestWeatherPerResort();

        // 3️⃣ Scoring
        List<RecommendedResortDTO> scored = new ArrayList<>();

        for (WeatherData weather : latestWeather) {
            SkiResort resort = weather.getSkiResort();

            if (!closestResorts.contains(resort)) continue;
            if (dangerousRegions.contains(resort.getBergfexName())) continue;

            double penalty = calculatePenalty(weather);
            double distance = distanceKm(
                    userLat, userLon,
                    resort.getLatitude(), resort.getLongitude()
            );

            scored.add(new RecommendedResortDTO(resort, distance, penalty));
        }

        // 4️⃣ Return top 5
        return scored.stream()
                .sorted(Comparator.comparingDouble(RecommendedResortDTO::getPenaltyScore))
                .limit(5)
                .collect(Collectors.toList());
    }

    /* ---------------- PENALTY MODEL ---------------- */

    private double calculatePenalty(WeatherData w) {
        double penalty = 0;

        // 🌨 Snow depth logic
        if (w.getSnowDepth() != null) {
            double snow = w.getSnowDepth();

            if (snow < MIN_GOOD_SNOW) {
                // Strong penalty for insufficient snow
                penalty += (MIN_GOOD_SNOW - snow) * 200;
            } else if (snow > MAX_GOOD_SNOW) {
                // Small consistent penalty for very deep snow
                penalty += 20;
            }
            // No penalty in the ideal range (30 cm – 1.5 m)
        }

        // 🌡 Temperature (unchanged)
        if (w.getTemperature() != null) {
            penalty += Math.abs(w.getTemperature() - IDEAL_TEMPERATURE) * 5;
        }

        // 🌬 Wind logic
        if (w.getWindSpeed() != null && w.getWindSpeed() > WIND_THRESHOLD) {
            penalty += (w.getWindSpeed() - WIND_THRESHOLD) * 3;
        }

        return penalty;
    }

    /* ---------------- HELPERS ---------------- */

    private boolean isDangerous(String level) {
        return level != null &&
                List.of("moderate", "considerable", "high", "very_high")
                        .contains(level.toLowerCase());
    }

    private double distanceKm(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);

        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
