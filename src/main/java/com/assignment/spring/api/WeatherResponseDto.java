package com.assignment.spring.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherResponseDto {

    private String city;

    private String country;

    private Double temperature;
}
