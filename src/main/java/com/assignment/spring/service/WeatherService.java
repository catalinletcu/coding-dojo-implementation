package com.assignment.spring.service;

import com.assignment.spring.api.WeatherResponseDto;
import com.assignment.spring.api.client.WeatherResponse;
import com.assignment.spring.entity.WeatherEntity;
import com.assignment.spring.entity.repository.WeatherRepository;
import com.assignment.spring.exception.BadRequestException;
import com.assignment.spring.mapper.WeatherMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class WeatherService {

    @Value("${weather.api.url}")
    private String weatherApiUrl;
    @Value("${weather.api.key}")
    private String weatherApiKey;

    private final WeatherRepository weatherRepository;
    private final WeatherMapper weatherMapper;
    private final RestTemplate restTemplate;

    public WeatherResponseDto saveWeather(String city) {
        final String url = weatherApiUrl.replace("{city}", city).replace("{appid}", weatherApiKey);

        final ResponseEntity<WeatherResponse> response = restTemplate.getForEntity(url, WeatherResponse.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new BadRequestException(String.format("Exception occurred when call external weather api for city %s", city));
        }
        final WeatherEntity weatherEntity = weatherRepository.save(weatherMapper.mapToEntity(response.getBody()));

        return weatherMapper.mapToDto(weatherEntity);
    }
}
