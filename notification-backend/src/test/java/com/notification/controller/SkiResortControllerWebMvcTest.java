package com.notification.controller;

import com.notification.service.SkiResortService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SkiResortController.class)
class SkiResortControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SkiResortService skiResortService;

    @Test
    void scrapeSkiResort_shouldCallService() throws Exception {
        mockMvc.perform(get("/api/skiresort/scrape"))
                .andExpect(status().isOk());

        verify(skiResortService).scrapeSkiResortStatusForAllResorts();
    }

    @Test
    void getAllLifts_shouldReturnOk() throws Exception {
        when(skiResortService.getAllSkiResortLiftData()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/skiresort/lifts"))
                .andExpect(status().isOk());

        verify(skiResortService).getAllSkiResortLiftData();
    }

    @Test
    void getAllSlopes_shouldReturnOk() throws Exception {
        when(skiResortService.getAllSkiResortSlopeData()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/skiresort/slopes"))
                .andExpect(status().isOk());

        verify(skiResortService).getAllSkiResortSlopeData();
    }

    @Test
    void getLiftsForResort_shouldReturnOk() throws Exception {
        when(skiResortService.getSkiResortLiftDataForResort(2L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/skiresort/resort/2/lifts"))
                .andExpect(status().isOk());

        verify(skiResortService).getSkiResortLiftDataForResort(2L);
    }

    @Test
    void getSlopesForResort_shouldReturnOk() throws Exception {
        when(skiResortService.getSkiResortSlopeDataForResort(2L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/skiresort/resort/2/slopes"))
                .andExpect(status().isOk());

        verify(skiResortService).getSkiResortSlopeDataForResort(2L);
    }
}
