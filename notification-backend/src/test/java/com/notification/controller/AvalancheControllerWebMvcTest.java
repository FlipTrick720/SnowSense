package com.notification.controller;

import com.notification.service.AvalancheService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AvalancheController.class)
class AvalancheControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AvalancheService avalancheService;

    @Test
    void triggerScrape_shouldCallService() throws Exception {
        mockMvc.perform(post("/api/avalanche/scrape"))
                .andExpect(status().isOk());

        verify(avalancheService).manualScrape();
    }

    @Test
    void getAllBulletins_shouldReturnOk() throws Exception {
        when(avalancheService.getAllAvalancheData()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/avalanche"))
                .andExpect(status().isOk());

        verify(avalancheService).getAllAvalancheData();
    }

    @Test
    void getCurrentBulletins_shouldReturnOk() throws Exception {
        when(avalancheService.getCurrentBulletins()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/avalanche/current"))
                .andExpect(status().isOk());

        verify(avalancheService).getCurrentBulletins();
    }

    @Test
    void getBulletinsForRegion_shouldReturnOk() throws Exception {
        when(avalancheService.getBulletinsForRegion("AT-01")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/avalanche/region/AT-01"))
                .andExpect(status().isOk());

        verify(avalancheService).getBulletinsForRegion("AT-01");
    }

    @Test
    void getHighDanger_shouldReturnOk() throws Exception {
        when(avalancheService.getHighDangerBulletins()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/avalanche/high-danger"))
                .andExpect(status().isOk());

        verify(avalancheService).getHighDangerBulletins();
    }
}
