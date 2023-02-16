package com.assignment.spring;

import com.assignment.spring.api.WeatherResponseDto;
import com.assignment.spring.api.client.WeatherResponse;
import com.assignment.spring.entity.WeatherEntity;

public abstract class AbstractTestUtil {

    public static final String CITY = "Bucharest";

    public static WeatherResponseDto getWeatherResponseDto(String city) {
        final WeatherResponseDto weatherResponseDto = new WeatherResponseDto();
        weatherResponseDto.setCity(city);

        return weatherResponseDto;
    }

    public static WeatherEntity getWeatherEntity(String city) {
        final WeatherEntity weatherEntity = new WeatherEntity();
        weatherEntity.setCity(city);

        return weatherEntity;
    }

    public static WeatherResponse getWeatherResponse(String city) {
        final WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.setName(city);

        return weatherResponse;
    }
}
