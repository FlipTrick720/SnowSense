package com.notification.service;

import com.notification.dto.OpenMeteoResponse;
import com.notification.model.SkiResort;
import com.notification.model.WeatherData;
import com.notification.repository.SkiResortRepository;
import com.notification.repository.WeatherDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private SkiResortRepository skiResortRepository;

    @Mock
    private WeatherDataRepository weatherDataRepository;

    @InjectMocks
    private WeatherService weatherService;

    @Test
    void getAllWeatherData_shouldDelegateToRepository() {
        when(weatherDataRepository.findAll()).thenReturn(List.of());
        assertNotNull(weatherService.getAllWeatherData());
        verify(weatherDataRepository).findAll();
    }

    @Test
    void getWeatherDataForResort_shouldDelegateToRepository() {
        when(weatherDataRepository.findBySkiResortIdOrderByTimestampDesc(1L)).thenReturn(List.of());
        assertNotNull(weatherService.getWeatherDataForResort(1L));
        verify(weatherDataRepository).findBySkiResortIdOrderByTimestampDesc(1L);
    }

    @Test
    void scrapeWeatherForAllResorts_shouldContinueWhenOneResortThrows() {
        SkiResort ok = new SkiResort(1L, "OK", "b", 47.0, 11.0, 1000, null);
        SkiResort bad = new SkiResort(2L, "BAD", "b", 47.0, 11.0, 1000, null);

        when(skiResortRepository.findAll()).thenReturn(List.of(ok, bad));

        // Use spy to throw from scrapeWeatherForResort for one resort
        WeatherService spy = spy(weatherService);
        doThrow(new RuntimeException("boom"))
                .when(spy)
                .scrapeWeatherForResort(bad);

        // and do nothing for ok (default is real method, which might try HTTP). So stub it too.
        doNothing().when(spy).scrapeWeatherForResort(ok);

        spy.scrapeWeatherForAllResorts();

        verify(skiResortRepository).findAll();
        verify(spy).scrapeWeatherForResort(ok);
        verify(spy).scrapeWeatherForResort(bad);
    }

    @Test
    void mapToWeatherData_shouldMapHourlyFirstValuesAndNulls() throws Exception {
        SkiResort resort = new SkiResort(1L, "R", "b", 47.0, 11.0, 1000, null);

        OpenMeteoResponse response = new OpenMeteoResponse();
        OpenMeteoResponse.CurrentWeather current = new OpenMeteoResponse.CurrentWeather();
        current.setTime("2024-01-01T10:00:00");
        current.setTemperature2m(1.5);
        current.setWindSpeed10m(10.0);
        current.setWindDirection10m(90);
        current.setWeatherCode(3);

        OpenMeteoResponse.HourlyWeather hourly = new OpenMeteoResponse.HourlyWeather();
        hourly.setPrecipitation(List.of(0.1));
        hourly.setSnowfall(List.of(0.2));
        hourly.setSnowDepth(List.of(10.0));
        hourly.setCloudCover(List.of(50));
        // leave visibility and freezingLevelHeight null -> should map to null

        response.setCurrent(current);
        response.setHourly(hourly);

        Method m = WeatherService.class.getDeclaredMethod("mapToWeatherData", SkiResort.class, OpenMeteoResponse.class);
        m.setAccessible(true);

        WeatherData data = (WeatherData) m.invoke(weatherService, resort, response);

        assertNotNull(data);
        assertEquals(resort, data.getSkiResort());
        assertEquals(LocalDateTime.parse("2024-01-01T10:00:00"), data.getTimestamp());
        assertEquals(1.5, data.getTemperature());
        assertEquals(10.0, data.getWindSpeed());
        assertEquals(90, data.getWindDirection());
        assertEquals(3, data.getWeatherCode());
        assertEquals(0.1, data.getPrecipitation());
        assertEquals(0.2, data.getSnowfall());
        assertEquals(10.0, data.getSnowDepth());
        assertEquals(50, data.getCloudCover());
        assertNull(data.getVisibility());
        assertNull(data.getFreezingLevel());
    }

    @Test
    void scrapeWeatherForResort_shouldSaveWhenResponseHasCurrent() throws Exception {
        // We won't hit the real HTTP; we replace restTemplate via reflection.
        var restTemplate = mock(org.springframework.web.client.RestTemplate.class);

        SkiResort resort = new SkiResort(1L, "R", "b", 47.0, 11.0, 1000, null);

        OpenMeteoResponse response = new OpenMeteoResponse();
        OpenMeteoResponse.CurrentWeather current = new OpenMeteoResponse.CurrentWeather();
        current.setTime("2024-01-01T10:00:00");
        current.setTemperature2m(1.0);
        current.setWindSpeed10m(2.0);
        current.setWindDirection10m(30);
        current.setWeatherCode(0);
        response.setCurrent(current);

        OpenMeteoResponse.HourlyWeather hourly = new OpenMeteoResponse.HourlyWeather();
        hourly.setPrecipitation(List.of());
        hourly.setSnowfall(List.of());
        hourly.setSnowDepth(List.of());
        hourly.setCloudCover(List.of());
        hourly.setVisibility(List.of());
        hourly.setFreezingLevelHeight(List.of());
        response.setHourly(hourly);

        when(restTemplate.getForObject(any(String.class), eq(OpenMeteoResponse.class))).thenReturn(response);

        // inject the mock restTemplate
        var f = WeatherService.class.getDeclaredField("restTemplate");
        f.setAccessible(true);
        f.set(weatherService, restTemplate);

        ArgumentCaptor<WeatherData> captor = ArgumentCaptor.forClass(WeatherData.class);

        weatherService.scrapeWeatherForResort(resort);

        verify(weatherDataRepository).save(captor.capture());
        assertEquals(resort, captor.getValue().getSkiResort());
    }

    @Test
    void scrapeWeatherForResort_shouldNotSaveWhenResponseNull() throws Exception {
        var restTemplate = mock(org.springframework.web.client.RestTemplate.class);
        when(restTemplate.getForObject(any(String.class), eq(OpenMeteoResponse.class))).thenReturn(null);

        var f = WeatherService.class.getDeclaredField("restTemplate");
        f.setAccessible(true);
        f.set(weatherService, restTemplate);

        SkiResort resort = new SkiResort(1L, "R", "b", 47.0, 11.0, 1000, null);
        weatherService.scrapeWeatherForResort(resort);

        verify(weatherDataRepository, never()).save(any());
    }
}
