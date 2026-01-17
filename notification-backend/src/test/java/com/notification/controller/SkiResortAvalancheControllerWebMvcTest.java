package com.notification.controller;

import com.notification.dto.SkiResortWithAvalancheDTO;
import com.notification.service.SkiResortAvalancheService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SkiResortAvalancheController.class)
class SkiResortAvalancheControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SkiResortAvalancheService service;

    @Test
    void getAllResortsWithAvalanche_shouldReturnOk() throws Exception {
        when(service.getAllResortsWithAvalancheData()).thenReturn(List.of());

        mockMvc.perform(get("/api/resorts/with-avalanche"))
                .andExpect(status().isOk());

        verify(service).getAllResortsWithAvalancheData();
    }

    @Test
    void getResortWithAvalanche_shouldReturnOk() throws Exception {
        when(service.getResortWithAvalancheData(1L)).thenReturn(SkiResortWithAvalancheDTO.builder().resortId(1L).build());

        mockMvc.perform(get("/api/resorts/1/with-avalanche"))
                .andExpect(status().isOk());

        verify(service).getResortWithAvalancheData(1L);
    }

    @Test
    void getSafeResorts_shouldReturnOk() throws Exception {
        when(service.getSafeResorts()).thenReturn(List.of());

        mockMvc.perform(get("/api/resorts/safe"))
                .andExpect(status().isOk());

        verify(service).getSafeResorts();
    }

    @Test
    void getHighDangerResorts_shouldReturnOk() throws Exception {
        when(service.getHighDangerResorts()).thenReturn(List.of());

        mockMvc.perform(get("/api/resorts/high-danger"))
                .andExpect(status().isOk());

        verify(service).getHighDangerResorts();
    }
}
