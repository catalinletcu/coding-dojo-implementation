package com.assignment.spring.mapper;

import com.assignment.spring.api.WeatherResponseDto;
import com.assignment.spring.api.client.Main;
import com.assignment.spring.api.client.Sys;
import com.assignment.spring.api.client.WeatherResponse;
import com.assignment.spring.entity.WeatherEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class WeatherMapper {

    public WeatherEntity mapToEntity(WeatherResponse source) {
        final WeatherEntity destination = new WeatherEntity();
        destination.setCity(source.getName());
        destination.setCountry(Optional.ofNullable(source.getSys()).map(Sys::getCountry).orElse(null));
        destination.setTemperature(Optional.ofNullable(source.getMain()).map(Main::getTemp).orElse(null));

        return destination;
    }

    public WeatherResponseDto mapToDto(WeatherEntity source) {
        final WeatherResponseDto destination = new WeatherResponseDto();
        destination.setCity(source.getCity());
        destination.setCountry(source.getCountry());
        destination.setTemperature(source.getTemperature());

        return destination;
    }
}
