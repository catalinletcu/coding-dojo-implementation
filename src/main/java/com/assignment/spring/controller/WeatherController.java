package com.assignment.spring.controller;

import com.assignment.spring.api.WeatherResponseDto;
import com.assignment.spring.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "weather")
public class WeatherController {

    private final WeatherService weatherService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<WeatherResponseDto> saveWeather(HttpServletRequest httpRequest) {
        final String city = httpRequest.getParameter("city");
        log.info("Request to save weather for city {} received.", city);

        final WeatherResponseDto responseDto = weatherService.saveWeather(city);

        log.info("Weather saved successfully for city {}", city);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}


