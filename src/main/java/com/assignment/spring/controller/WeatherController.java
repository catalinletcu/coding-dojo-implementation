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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "weather")
public class WeatherController {

    private final WeatherService weatherService;

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<WeatherResponseDto> saveWeather(@Valid @NotNull @RequestParam(value = "city") String city) {
        log.info("Request to save weather for city {} received.", city);

        final WeatherResponseDto responseDto = weatherService.saveWeather(city);

        log.info("Weather saved successfully for city {}", city);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}


