package com.notification.controller;

import com.notification.model.WeatherData;
import com.notification.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    
    private final WeatherService weatherService;
    
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }
    
    /**
     * Trigger weather scraping for all ski resorts
     * POST /api/weather/scrape
     * 
     * Note: Weather data is automatically scraped every 15 minutes
     */
    @PostMapping("/scrape")
    public ResponseEntity<Map<String, String>> scrapeWeather() {
        weatherService.scrapeWeatherForAllResorts();
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Weather data scrape triggered successfully");
        response.put("note", "Automatic scraping runs every 15 minutes");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get all weather data from database
     * GET /api/weather
     */
    @GetMapping
    public ResponseEntity<List<WeatherData>> getAllWeatherData() {
        return ResponseEntity.ok(weatherService.getAllWeatherData());
    }
    
    /**
     * Get weather data for a specific resort
     * GET /api/weather/resort/{resortId}
     */
    @GetMapping("/resort/{resortId}")
    public ResponseEntity<List<WeatherData>> getWeatherForResort(@PathVariable Long resortId) {
        return ResponseEntity.ok(weatherService.getWeatherDataForResort(resortId));
    }
}
