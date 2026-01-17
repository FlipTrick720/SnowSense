package com.notification.service;

import com.notification.dto.CAAMLResponse;
import com.notification.model.AvalancheData;
import com.notification.repository.AvalancheDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvalancheServiceTest {

    @Mock
    private AvalancheDataRepository avalancheDataRepository;

    @InjectMocks
    private AvalancheService avalancheService;

    @Test
    void getAllAvalancheData_shouldDelegate() {
        when(avalancheDataRepository.findAll()).thenReturn(List.of());
        assertNotNull(avalancheService.getAllAvalancheData());
        verify(avalancheDataRepository).findAll();
    }

    @Test
    void getCurrentBulletins_shouldDelegate() {
        when(avalancheDataRepository.findCurrentlyValid(any(LocalDateTime.class))).thenReturn(List.of());
        assertNotNull(avalancheService.getCurrentBulletins());
        verify(avalancheDataRepository).findCurrentlyValid(any(LocalDateTime.class));
    }

    @Test
    void getBulletinsForRegion_shouldDelegate() {
        when(avalancheDataRepository.findByRegionCode("AT-01")).thenReturn(List.of());
        assertNotNull(avalancheService.getBulletinsForRegion("AT-01"));
        verify(avalancheDataRepository).findByRegionCode("AT-01");
    }

    @Test
    void getHighDangerBulletins_shouldDelegate() {
        when(avalancheDataRepository.findHighDangerBulletins(any(LocalDateTime.class))).thenReturn(List.of());
        assertNotNull(avalancheService.getHighDangerBulletins());
        verify(avalancheDataRepository).findHighDangerBulletins(any(LocalDateTime.class));
    }

    @Test
    void mapToAvalancheData_shouldMapRegionsDangerRatingsProblemsAndText() throws Exception {
        CAAMLResponse.Bulletin b = new CAAMLResponse.Bulletin();
        b.setBulletinID("b1");
        b.setPublicationTime(LocalDateTime.of(2024, 1, 1, 10, 0));
        b.setUnscheduled(false);
        b.setHighlights("h");
        b.setComment("c");

        CAAMLResponse.ValidTime vt = new CAAMLResponse.ValidTime();
        vt.setStartTime(LocalDateTime.of(2024, 1, 1, 0, 0));
        vt.setEndTime(LocalDateTime.of(2024, 1, 2, 0, 0));
        b.setValidTime(vt);

        CAAMLResponse.Region r1 = new CAAMLResponse.Region();
        r1.setRegionID("AT-01");
        CAAMLResponse.Region r2 = new CAAMLResponse.Region();
        r2.setRegionID("AT-02");
        b.setRegions(List.of(r1, r2));

        CAAMLResponse.DangerRating drMain = new CAAMLResponse.DangerRating();
        drMain.setMainValue("high");
        drMain.setValidTimePeriod("all_day");
        CAAMLResponse.Elevation elev = new CAAMLResponse.Elevation();
        elev.setLowerBound("1000");
        elev.setUpperBound("2000");
        drMain.setElevation(elev);
        drMain.setAspects(List.of("N", "E"));

        CAAMLResponse.DangerRating drLater = new CAAMLResponse.DangerRating();
        drLater.setMainValue("very_high");
        drLater.setValidTimePeriod("later");

        b.setDangerRatings(List.of(drLater, drMain));

        CAAMLResponse.AvalancheProblem p = new CAAMLResponse.AvalancheProblem();
        p.setProblemType("new_snow");
        b.setAvalancheProblems(List.of(p));

        CAAMLResponse.TextContent tc = new CAAMLResponse.TextContent();
        tc.setHighlights("hh");
        tc.setComment("cc");
        b.setAvalancheActivity(tc);

        CAAMLResponse.Tendency t = new CAAMLResponse.Tendency();
        t.setTendencyType("increasing");
        b.setTendency(List.of(t));

        Method m = AvalancheService.class.getDeclaredMethod("mapToAvalancheData", CAAMLResponse.Bulletin.class);
        m.setAccessible(true);

        AvalancheData data = (AvalancheData) m.invoke(avalancheService, b);

        assertEquals("b1", data.getBulletinId());
        assertEquals(vt.getStartTime(), data.getValidTimeStart());
        assertEquals(vt.getEndTime(), data.getValidTimeEnd());
        assertEquals("AT-01,AT-02", data.getRegionCodes());
        assertEquals("high", data.getDangerLevel());
        assertEquals("very_high", data.getDangerLevelAfternoon());
        assertEquals("1000", data.getElevationLower());
        assertEquals("2000", data.getElevationUpper());
        assertEquals("N,E", data.getAspects());
        assertEquals("new_snow", data.getProblemTypes());
        assertEquals("hh | cc", data.getAvalancheActivity());
        assertEquals("increasing", data.getTendencyType());
        // assertNotNull(data.getRawData());
    }

    @Test
    void scrapeAvalancheBulletin_shouldUpdateExisting() throws Exception {
        CAAMLResponse.Bulletin b = new CAAMLResponse.Bulletin();
        b.setBulletinID("b1");
        b.setPublicationTime(LocalDateTime.of(2024, 1, 1, 10, 0));
        CAAMLResponse.ValidTime vt = new CAAMLResponse.ValidTime();
        vt.setStartTime(LocalDateTime.of(2024, 1, 1, 0, 0));
        vt.setEndTime(LocalDateTime.of(2024, 1, 2, 0, 0));
        b.setValidTime(vt);

        AvalancheData existing = AvalancheData.builder()
                .id(55L)
                .bulletinId("b1")
                .publicationTime(LocalDateTime.of(2024, 1, 1, 9, 0))
                .validTimeStart(vt.getStartTime())
                .validTimeEnd(vt.getEndTime())
                .createdAt(LocalDateTime.of(2024, 1, 1, 9, 0))
                .build();

        when(avalancheDataRepository.findByBulletinId("b1")).thenReturn(Optional.of(existing));

        ArgumentCaptor<AvalancheData> captor = ArgumentCaptor.forClass(AvalancheData.class);
        when(avalancheDataRepository.save(any(AvalancheData.class))).thenAnswer(i -> i.getArgument(0));

        Method m = AvalancheService.class.getDeclaredMethod("scrapeAvalancheBulletin", CAAMLResponse.Bulletin.class);
        m.setAccessible(true);
        m.invoke(avalancheService, b);

        verify(avalancheDataRepository).save(captor.capture());
        assertEquals(55L, captor.getValue().getId());
        assertEquals(existing.getCreatedAt(), captor.getValue().getCreatedAt());
    }

    @Test
    void scrapeAvalancheBulletin_shouldInsertNewWhenNotExists() throws Exception {
        CAAMLResponse.Bulletin b = new CAAMLResponse.Bulletin();
        b.setBulletinID("b1");
        b.setPublicationTime(LocalDateTime.of(2024, 1, 1, 10, 0));
        CAAMLResponse.ValidTime vt = new CAAMLResponse.ValidTime();
        vt.setStartTime(LocalDateTime.of(2024, 1, 1, 0, 0));
        vt.setEndTime(LocalDateTime.of(2024, 1, 2, 0, 0));
        b.setValidTime(vt);

        when(avalancheDataRepository.findByBulletinId("b1")).thenReturn(Optional.empty());
        when(avalancheDataRepository.save(any(AvalancheData.class))).thenAnswer(i -> i.getArgument(0));

        Method m = AvalancheService.class.getDeclaredMethod("scrapeAvalancheBulletin", CAAMLResponse.Bulletin.class);
        m.setAccessible(true);
        m.invoke(avalancheService, b);

        verify(avalancheDataRepository).save(any(AvalancheData.class));
    }
}
