package com.notification.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class OpenMeteoResponse {
    
    private Double latitude;
    private Double longitude;
    private Integer elevation;
    
    @JsonProperty("current")
    private CurrentWeather current;
    
    @JsonProperty("hourly")
    private HourlyWeather hourly;
    
    @Data
    public static class CurrentWeather {
        private String time;
        
        @JsonProperty("temperature_2m")
        private Double temperature2m;
        
        @JsonProperty("wind_speed_10m")
        private Double windSpeed10m;
        
        @JsonProperty("wind_direction_10m")
        private Integer windDirection10m;
        
        @JsonProperty("weather_code")
        private Integer weatherCode;
    }
    
    @Data
    public static class HourlyWeather {
        private List<String> time;
        
        @JsonProperty("temperature_2m")
        private List<Double> temperature2m;
        
        @JsonProperty("precipitation")
        private List<Double> precipitation;
        
        @JsonProperty("snowfall")
        private List<Double> snowfall;
        
        @JsonProperty("snow_depth")
        private List<Double> snowDepth;
        
        @JsonProperty("cloud_cover")
        private List<Integer> cloudCover;
        
        @JsonProperty("visibility")
        private List<Double> visibility;
        
        @JsonProperty("wind_speed_10m")
        private List<Double> windSpeed10m;
        
        @JsonProperty("wind_direction_10m")
        private List<Integer> windDirection10m;
        
        @JsonProperty("freezing_level_height")
        private List<Integer> freezingLevelHeight;
    }
}
