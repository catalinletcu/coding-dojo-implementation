package com.assignment.spring.service;

import com.assignment.spring.AbstractTestUtil;
import com.assignment.spring.api.WeatherResponseDto;
import com.assignment.spring.api.client.WeatherResponse;
import com.assignment.spring.entity.WeatherEntity;
import com.assignment.spring.entity.repository.WeatherRepository;
import com.assignment.spring.exception.BadRequestException;
import com.assignment.spring.mapper.WeatherMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest extends AbstractTestUtil {

    @Mock
    WeatherRepository weatherRepository;

    @Mock
    WeatherMapper weatherMapper;

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    WeatherService weatherService;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(weatherService, "weatherApiUrl", "url");
        ReflectionTestUtils.setField(weatherService, "weatherApiKey", "encodedKey");
    }

    @Test
    void saveWeatherWithValidCityWhenExternalApiWorksFineThenReturnSavedCity() {
        final WeatherResponse weatherResponse = getWeatherResponse(CITY);
        final WeatherEntity weatherEntity = getWeatherEntity(CITY);
        final WeatherResponseDto weatherResponseDto = getWeatherResponseDto(CITY);

        when(restTemplate.getForEntity("url", WeatherResponse.class)).thenReturn(ResponseEntity.ok(weatherResponse));
        when(weatherMapper.mapToEntity(weatherResponse)).thenReturn(weatherEntity);
        when(weatherRepository.save(weatherEntity)).thenReturn(weatherEntity);
        when(weatherMapper.mapToDto(weatherEntity)).thenReturn(weatherResponseDto);

        final WeatherResponseDto responseDto = weatherService.saveWeather(CITY);

        assertNotNull(responseDto);
        assertEquals(CITY, responseDto.getCity());
        verify(restTemplate, times(1)).getForEntity("url", WeatherResponse.class);
    }

    @Test
    void saveWeatherWithValidCityWhenExternalApiReturnsEmptyBodyThenThrowException() {
        when(restTemplate.getForEntity("url", WeatherResponse.class)).thenReturn(ResponseEntity.ok(null));

        assertThrows(BadRequestException.class, () -> weatherService.saveWeather(CITY),
                "Exception occurred when call external service for city Bucharest");
    }
}
