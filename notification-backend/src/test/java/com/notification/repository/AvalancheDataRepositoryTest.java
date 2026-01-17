package com.notification.repository;

import com.notification.model.AvalancheData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AvalancheDataRepositoryTest {

    @Autowired
    private AvalancheDataRepository repository;

    @Test
    void queries_shouldWork() {
        LocalDateTime now = LocalDateTime.now();

        AvalancheData a1 = AvalancheData.builder()
                .bulletinId("b1")
                .publicationTime(now.minusHours(1))
                .validTimeStart(now.minusHours(2))
                .validTimeEnd(now.plusHours(2))
                .regionCodes("AT-01,AT-02")
                .dangerLevel("high")
                .build();

        AvalancheData a2 = AvalancheData.builder()
                .bulletinId("b2")
                .publicationTime(now.minusDays(1))
                .validTimeStart(now.minusDays(2))
                .validTimeEnd(now.minusDays(1))
                .regionCodes("AT-03")
                .dangerLevel("low")
                .build();

        repository.saveAll(List.of(a1, a2));

        assertTrue(repository.findByBulletinId("b1").isPresent());
        assertEquals(1, repository.findCurrentlyValid(now).size());
        assertEquals(1, repository.findByRegionCode("AT-01").size());
        assertEquals(1, repository.findHighDangerBulletins(now).size());
        assertEquals(1, repository.findByPublicationTimeAfter(now.minusHours(2)).size());
    }
}
