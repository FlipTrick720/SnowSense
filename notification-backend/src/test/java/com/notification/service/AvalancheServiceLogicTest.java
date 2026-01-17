
package com.notification.service;

import com.notification.dto.CAAMLResponse;
import com.notification.model.AvalancheData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

public class AvalancheServiceLogicTest {

    private AvalancheService avalancheService;

    @BeforeEach
    public void setUp() {
        // We don't need a real repository for this test
        avalancheService = new AvalancheService(null);
    }

    @Test
    public void testExtractDangerRatings_HighestFirst() {
        CAAMLResponse.DangerRating low = new CAAMLResponse.DangerRating("low", null, "all_day", null);
        CAAMLResponse.DangerRating moderate = new CAAMLResponse.DangerRating("moderate", null, "all_day", null);
        CAAMLResponse.DangerRating considerable = new CAAMLResponse.DangerRating("considerable", null, "all_day", null);

        List<CAAMLResponse.DangerRating> ratings = Arrays.asList(considerable, moderate, low);
        AvalancheData.AvalancheDataBuilder builder = AvalancheData.builder();

        avalancheService.extractDangerRatings(ratings, builder);

        AvalancheData data = builder.build();
        assertEquals("considerable", data.getDangerLevel());
    }

    @Test
    public void testExtractDangerRatings_HighestInMiddle() {
        CAAMLResponse.DangerRating low = new CAAMLResponse.DangerRating("low", null, "all_day", null);
        CAAMLResponse.DangerRating moderate = new CAAMLResponse.DangerRating("moderate", null, "all_day", null);
        CAAMLResponse.DangerRating considerable = new CAAMLResponse.DangerRating("considerable", null, "all_day", null);

        List<CAAMLResponse.DangerRating> ratings = Arrays.asList(low, considerable, moderate);
        AvalancheData.AvalancheDataBuilder builder = AvalancheData.builder();

        avalancheService.extractDangerRatings(ratings, builder);

        AvalancheData data = builder.build();
        assertEquals("considerable", data.getDangerLevel());
    }

    @Test
    public void testExtractDangerRatings_HighestLast() {
        CAAMLResponse.DangerRating low = new CAAMLResponse.DangerRating("low", null, "all_day", null);
        CAAMLResponse.DangerRating moderate = new CAAMLResponse.DangerRating("moderate", null, "all_day", null);
        CAAMLResponse.DangerRating considerable = new CAAMLResponse.DangerRating("considerable", null, "all_day", null);

        List<CAAMLResponse.DangerRating> ratings = Arrays.asList(low, moderate, considerable);
        AvalancheData.AvalancheDataBuilder builder = AvalancheData.builder();

        avalancheService.extractDangerRatings(ratings, builder);

        AvalancheData data = builder.build();
        assertEquals("considerable", data.getDangerLevel());
    }

    @Test
    public void testExtractDangerRatings_SingleRating() {
        CAAMLResponse.DangerRating moderate = new CAAMLResponse.DangerRating("moderate", null, "all_day", null);

        List<CAAMLResponse.DangerRating> ratings = Arrays.asList(moderate);
        AvalancheData.AvalancheDataBuilder builder = AvalancheData.builder();

        avalancheService.extractDangerRatings(ratings, builder);

        AvalancheData data = builder.build();
        assertEquals("moderate", data.getDangerLevel());
    }

    @Test
    public void testExtractDangerRatings_WithDuplicates() {
        CAAMLResponse.DangerRating low = new CAAMLResponse.DangerRating("low", null, "all_day", null);
        CAAMLResponse.DangerRating moderate = new CAAMLResponse.DangerRating("moderate", null, "all_day", null);
        CAAMLResponse.DangerRating considerable = new CAAMLResponse.DangerRating("considerable", null, "all_day", null);

        List<CAAMLResponse.DangerRating> ratings = Arrays.asList(low, moderate, considerable, considerable, moderate);
        AvalancheData.AvalancheDataBuilder builder = AvalancheData.builder();

        avalancheService.extractDangerRatings(ratings, builder);

        AvalancheData data = builder.build();
        assertEquals("considerable", data.getDangerLevel());
    }
}

