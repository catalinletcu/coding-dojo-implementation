package com.assignment.spring.service;

import com.assignment.spring.Constants;
import com.assignment.spring.WeatherEntity;
import com.assignment.spring.WeatherRepository;
import com.assignment.spring.api.WeatherResponse;
import com.assignment.spring.api.WeatherResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherRepository weatherRepository;

    private final RestTemplate restTemplate;

    public WeatherResponseDto saveWeather(String city) {
        final String url = Constants.WEATHER_API_URL.replace("{city}", city).replace("{appid}", Constants.APP_ID);
        ResponseEntity<WeatherResponse> response = restTemplate.getForEntity(url, WeatherResponse.class);

        
    }

    private WeatherEntity mapper(WeatherResponse response) {
        WeatherEntity entity = new WeatherEntity();
        entity.setCity(response.getName());
        entity.setCountry(response.getSys().getCountry());
        entity.setTemperature(response.getMain().getTemp());

        return weatherRepository.save(entity);
    }
}
