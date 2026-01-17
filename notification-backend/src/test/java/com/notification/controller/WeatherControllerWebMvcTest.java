package com.notification.controller;

import com.notification.service.WeatherService;
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

@WebMvcTest(WeatherController.class)
class WeatherControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @Test
    void scrapeWeather_shouldCallService() throws Exception {
        mockMvc.perform(post("/api/weather/scrape"))
                .andExpect(status().isOk());

        verify(weatherService).scrapeWeatherForAllResorts();
    }

    @Test
    void getAllWeatherData_shouldReturnOk() throws Exception {
        when(weatherService.getAllWeatherData()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/weather"))
                .andExpect(status().isOk());

        verify(weatherService).getAllWeatherData();
    }

    @Test
    void getWeatherForResort_shouldReturnOk() throws Exception {
        when(weatherService.getWeatherDataForResort(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/weather/resort/1"))
                .andExpect(status().isOk());

        verify(weatherService).getWeatherDataForResort(1L);
    }
}
