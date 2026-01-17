package com.notification.service;

import com.notification.dto.SkiResortWithAvalancheDTO;
import com.notification.model.AvalancheData;
import com.notification.model.SkiResort;
import com.notification.model.SkiResortAvalancheRegion;
import com.notification.repository.AvalancheDataRepository;
import com.notification.repository.SkiResortAvalancheRegionRepository;
import com.notification.repository.SkiResortRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SkiResortAvalancheServiceTest {

    @Mock
    private SkiResortRepository skiResortRepository;

    @Mock
    private AvalancheDataRepository avalancheDataRepository;

    @Mock
    private SkiResortAvalancheRegionRepository regionMappingRepository;

    @InjectMocks
    private SkiResortAvalancheService service;

    @Test
    void getResortWithAvalancheData_shouldReturnUnknownWhenNoRegionMapping() {
        SkiResort resort = new SkiResort(1L, "Resort", "bergfex", 47.0, 11.0, 1000, null);
        when(skiResortRepository.findById(1L)).thenReturn(Optional.of(resort));
        when(regionMappingRepository.findPrimaryRegionForResort(1L)).thenReturn(null);

        SkiResortWithAvalancheDTO dto = service.getResortWithAvalancheData(1L);

        assertEquals("UNKNOWN", dto.getSafetyStatus());
        assertEquals("No avalanche region mapping available", dto.getRecommendation());
    }

    @Test
    void getResortWithAvalancheData_shouldReturnSafeForLowDanger() {
        SkiResort resort = new SkiResort(1L, "Resort", "bergfex", 47.0, 11.0, 1000, null);
        when(skiResortRepository.findById(1L)).thenReturn(Optional.of(resort));

        SkiResortAvalancheRegion mapping = SkiResortAvalancheRegion.builder()
                .id(10L)
                .skiResort(resort)
                .regionCode("AT-01")
                .regionName("Region")
                .isPrimary(true)
                .build();
        when(regionMappingRepository.findPrimaryRegionForResort(1L)).thenReturn(mapping);

        AvalancheData bulletin = AvalancheData.builder()
                .id(100L)
                .bulletinId("b1")
                .publicationTime(LocalDateTime.now().minusHours(1))
                .validTimeStart(LocalDateTime.now().minusHours(2))
                .validTimeEnd(LocalDateTime.now().plusHours(6))
                .regionCodes("AT-01,AT-02")
                .dangerLevel("low")
                .highlights("ok")
                .build();

        when(avalancheDataRepository.findCurrentlyValid(org.mockito.ArgumentMatchers.any(LocalDateTime.class)))
                .thenReturn(List.of(bulletin));

        SkiResortWithAvalancheDTO dto = service.getResortWithAvalancheData(1L);

        assertEquals("SAFE", dto.getSafetyStatus());
        assertNotNull(dto.getRecommendation());
        assertEquals("AT-01", dto.getAvalancheRegionCode());
        assertEquals("low", dto.getDangerLevel());
    }

    @Test
    void getSafeResorts_shouldFilterToOnlySafe() {
        SkiResort resortSafe = new SkiResort(1L, "R1", "b", 47.0, 11.0, 1000, null);
        SkiResort resortDanger = new SkiResort(2L, "R2", "b", 47.0, 11.0, 1000, null);

        when(skiResortRepository.findAll()).thenReturn(List.of(resortSafe, resortDanger));
        when(skiResortRepository.findById(1L)).thenReturn(Optional.of(resortSafe));
        when(skiResortRepository.findById(2L)).thenReturn(Optional.of(resortDanger));

        SkiResortAvalancheRegion m1 = SkiResortAvalancheRegion.builder().skiResort(resortSafe).regionCode("A").regionName("A").isPrimary(true).build();
        SkiResortAvalancheRegion m2 = SkiResortAvalancheRegion.builder().skiResort(resortDanger).regionCode("B").regionName("B").isPrimary(true).build();
        when(regionMappingRepository.findPrimaryRegionForResort(1L)).thenReturn(m1);
        when(regionMappingRepository.findPrimaryRegionForResort(2L)).thenReturn(m2);

        AvalancheData low = AvalancheData.builder()
                .bulletinId("b1")
                .publicationTime(LocalDateTime.now())
                .validTimeStart(LocalDateTime.now().minusHours(1))
                .validTimeEnd(LocalDateTime.now().plusHours(1))
                .regionCodes("A")
                .dangerLevel("low")
                .build();

        AvalancheData high = AvalancheData.builder()
                .bulletinId("b2")
                .publicationTime(LocalDateTime.now())
                .validTimeStart(LocalDateTime.now().minusHours(1))
                .validTimeEnd(LocalDateTime.now().plusHours(1))
                .regionCodes("B")
                .dangerLevel("high")
                .build();

        when(avalancheDataRepository.findCurrentlyValid(org.mockito.ArgumentMatchers.any(LocalDateTime.class)))
                .thenReturn(List.of(low, high));

        List<SkiResortWithAvalancheDTO> safe = service.getSafeResorts();
        assertEquals(1, safe.size());
        assertEquals(1L, safe.get(0).getResortId());
    }
}
