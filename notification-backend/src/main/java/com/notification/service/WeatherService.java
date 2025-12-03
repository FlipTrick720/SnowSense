package com.notification.service;

import com.notification.dto.OpenMeteoResponse;
import com.notification.model.SkiResort;
import com.notification.model.WeatherData;
import com.notification.repository.SkiResortRepository;
import com.notification.repository.WeatherDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class WeatherService {
    
    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);
    private static final String OPEN_METEO_API = "https://api.open-meteo.com/v1/forecast";
    
    private final SkiResortRepository skiResortRepository;
    private final WeatherDataRepository weatherDataRepository;
    private final RestTemplate restTemplate;
    
    public WeatherService(SkiResortRepository skiResortRepository,
                         WeatherDataRepository weatherDataRepository) {
        this.skiResortRepository = skiResortRepository;
        this.weatherDataRepository = weatherDataRepository;
        this.restTemplate = new RestTemplate();
    }
    
    /**
     * Scrape weather data for all ski resorts
     * Scheduled to run every 15 minutes
     */
    @Scheduled(cron = "0 */15 * * * *")  // Every 15 minutes
    public void scrapeWeatherForAllResorts() {
        List<SkiResort> resorts = skiResortRepository.findAll();
        logger.info("Starting weather scrape for {} ski resorts", resorts.size());
        
        for (SkiResort resort : resorts) {
            try {
                scrapeWeatherForResort(resort);
            } catch (Exception e) {
                logger.error("Failed to scrape weather for {}: {}", resort.getName(), e.getMessage());
            }
        }
        
        logger.info("Weather scrape completed");
    }
    
    /**
     * Scrape weather data for a specific ski resort
     */
    public void scrapeWeatherForResort(SkiResort resort) {
        logger.info("Scraping weather for: {} (lat: {}, lon: {})", 
                   resort.getName(), resort.getLatitude(), resort.getLongitude());
        
        String url = buildOpenMeteoUrl(resort);
        OpenMeteoResponse response = restTemplate.getForObject(url, OpenMeteoResponse.class);
        
        if (response != null && response.getCurrent() != null) {
            WeatherData weatherData = mapToWeatherData(resort, response);
            weatherDataRepository.save(weatherData);
            
            printWeatherData(resort, weatherData);
        }
    }
    
    /**
     * Build Open-Meteo API URL with all skiing-relevant parameters
     */
    private String buildOpenMeteoUrl(SkiResort resort) {
        return String.format(
            "%s?latitude=%.4f&longitude=%.4f" +
            "&current=temperature_2m,wind_speed_10m,wind_direction_10m,weather_code" +
            "&hourly=temperature_2m,precipitation,snowfall,snow_depth,cloud_cover," +
            "visibility,wind_speed_10m,wind_direction_10m,freezing_level_height" +
            "&timezone=Europe/Vienna" +
            "&forecast_days=1",
            OPEN_METEO_API,
            resort.getLatitude(),
            resort.getLongitude()
        );
    }
    
    /**
     * Map Open-Meteo response to WeatherData entity
     */
    private WeatherData mapToWeatherData(SkiResort resort, OpenMeteoResponse response) {
        OpenMeteoResponse.CurrentWeather current = response.getCurrent();
        OpenMeteoResponse.HourlyWeather hourly = response.getHourly();
        
        return WeatherData.builder()
                .skiResort(resort)
                .timestamp(LocalDateTime.parse(current.getTime(), 
                          DateTimeFormatter.ISO_DATE_TIME))
                .temperature(current.getTemperature2m())
                .windSpeed(current.getWindSpeed10m())
                .windDirection(current.getWindDirection10m())
                .weatherCode(current.getWeatherCode())
                // Get first hourly values (current hour)
                .precipitation(hourly.getPrecipitation() != null && !hourly.getPrecipitation().isEmpty() 
                              ? hourly.getPrecipitation().get(0) : null)
                .snowfall(hourly.getSnowfall() != null && !hourly.getSnowfall().isEmpty() 
                         ? hourly.getSnowfall().get(0) : null)
                .snowDepth(hourly.getSnowDepth() != null && !hourly.getSnowDepth().isEmpty() 
                          ? hourly.getSnowDepth().get(0) : null)
                .cloudCover(hourly.getCloudCover() != null && !hourly.getCloudCover().isEmpty() 
                           ? hourly.getCloudCover().get(0) : null)
                .visibility(hourly.getVisibility() != null && !hourly.getVisibility().isEmpty() 
                           ? hourly.getVisibility().get(0) : null)
                .freezingLevel(hourly.getFreezingLevelHeight() != null && !hourly.getFreezingLevelHeight().isEmpty() 
                              ? hourly.getFreezingLevelHeight().get(0) : null)
                .build();
    }
    
    /**
     * Print weather data to terminal
     */
    private void printWeatherData(SkiResort resort, WeatherData data) {
        logger.info("========================================");
        logger.info("Weather Data for: {}", resort.getName());
        logger.info("========================================");
        logger.info("Timestamp: {}", data.getTimestamp());
        logger.info("Temperature: {}°C", data.getTemperature());
        logger.info("Wind Speed: {} km/h", data.getWindSpeed());
        logger.info("Wind Direction: {}°", data.getWindDirection());
        logger.info("Precipitation: {} mm", data.getPrecipitation());
        logger.info("Snowfall: {} cm", data.getSnowfall());
        logger.info("Snow Depth: {} cm", data.getSnowDepth());
        logger.info("Cloud Cover: {}%", data.getCloudCover());
        logger.info("Visibility: {} m", data.getVisibility());
        logger.info("Freezing Level: {} m", data.getFreezingLevel());
        logger.info("Weather Code: {}", data.getWeatherCode());
        logger.info("========================================\n");
    }
    
    /**
     * Get all weather data from database
     */
    public List<WeatherData> getAllWeatherData() {
        return weatherDataRepository.findAll();
    }
    
    /**
     * Get weather data for a specific resort
     */
    public List<WeatherData> getWeatherDataForResort(Long resortId) {
        return weatherDataRepository.findBySkiResortIdOrderByTimestampDesc(resortId);
    }
}
