package com.notification.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for CAAML v6 avalanche bulletin response
 * Source: https://static.avalanche.report/bulletins/latest/EUREGIO_de_CAAMLv6.json
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CAAMLResponse {
    
    private List<Bulletin> bulletins;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Bulletin {
        private String bulletinID;
        private LocalDateTime publicationTime;
        private ValidTime validTime;
        private Boolean unscheduled;
        private List<Region> regions;
        private List<DangerRating> dangerRatings;
        private List<AvalancheProblem> avalancheProblems;
        private String highlights;
        private String comment;
        private TextContent avalancheActivity;
        private TextContent snowpackStructure;
        private TextContent travelAdvisory;
        private List<Tendency> tendency;
        private TextContent wxSynopsis;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Region {
        private String regionID;
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ValidTime {
        private LocalDateTime startTime;
        private LocalDateTime endTime;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DangerRating {
        private String mainValue; // low, moderate, considerable, high, very_high
        private Elevation elevation;
        private String validTimePeriod; // earlier, later, all_day
        private List<String> aspects; // N, NE, E, SE, S, SW, W, NW
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Elevation {
        private String lowerBound;
        private String upperBound;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AvalancheProblem {
        private String problemType; // wind_slab, new_snow, wet_snow, gliding_snow, persistent_weak_layers
        private Elevation elevation;
        private List<String> aspects;
        private String validTimePeriod;
        private String snowpackStability; // poor, fair, good
        private String frequency; // few, some, many
        private Integer avalancheSize; // 1-5
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TextContent {
        private String highlights;
        private String comment;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Tendency {
        private String tendencyType; // steady, increasing, decreasing
        private ValidTime validTime;
    }
}
