package com.notification.repository;

import com.notification.model.SkiResort;
import com.notification.model.SkiResortAvalancheRegion;
import com.notification.model.SkiResortLift;
import com.notification.model.SkiResortSlope;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SkiResortRepositoriesTest {

    @Autowired
    private SkiResortRepository skiResortRepository;

    @Autowired
    private SkiResortLiftRepository skiResortLiftRepository;

    @Autowired
    private SkiResortSlopeRepository skiResortSlopeRepository;

    @Autowired
    private SkiResortAvalancheRegionRepository regionRepository;

    @Test
    void repositories_shouldPersistAndQuery() {
        SkiResort resort = new SkiResort();
        resort.setName("R");
        resort.setBergfexName("b");
        resort.setLatitude(47.0);
        resort.setLongitude(11.0);
        resort.setElevation(1000);
        resort = skiResortRepository.save(resort);

        SkiResortLift lift = new SkiResortLift();
        lift.setSkiResort(resort);
        lift.setName("Lift");
        lift.setType("chair");
        lift.setLengthInMeters(1000);
        lift.setIsOpen(true);
        skiResortLiftRepository.save(lift);

        SkiResortSlope slope = new SkiResortSlope();
        slope.setSkiResort(resort);
        slope.setName("Slope");
        slope.setDifficultyLevel("easy");
        slope.setIsOpen(true);
        skiResortSlopeRepository.save(slope);

        assertEquals(1, skiResortLiftRepository.findBySkiResortIdOrderByCreatedAtDesc(resort.getId()).size());
        assertEquals(1, skiResortSlopeRepository.findBySkiResortIdOrderByCreatedAtDesc(resort.getId()).size());

        SkiResortAvalancheRegion mapping = new SkiResortAvalancheRegion();
        mapping.setSkiResort(resort);
        mapping.setRegionCode("AT-01");
        mapping.setRegionName("Region");
        mapping.setIsPrimary(true);
        regionRepository.save(mapping);

        assertEquals(1, regionRepository.findBySkiResortId(resort.getId()).size());
        assertEquals(1, regionRepository.findByRegionCode("AT-01").size());
        assertNotNull(regionRepository.findPrimaryRegionForResort(resort.getId()));
        assertEquals(1, regionRepository.findByRegionCodeIn(List.of("AT-01")).size());
    }
}
