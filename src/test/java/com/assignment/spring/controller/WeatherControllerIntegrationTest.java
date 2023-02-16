package com.assignment.spring.controller;

import com.assignment.spring.AbstractTestUtil;
import com.assignment.spring.WeatherApplication;
import com.assignment.spring.api.ApiError;
import com.assignment.spring.api.WeatherResponseDto;
import com.assignment.spring.entity.repository.WeatherRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;

import static com.assignment.spring.api.ApiError.BAD_REQUEST;
import static com.assignment.spring.api.ApiError.NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest(classes = WeatherApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.yml")
class WeatherControllerIntegrationTest extends AbstractTestUtil {

    protected static final String HOST = "localhost";
    protected static final String HTTP_PROTOCOL = "http";

    @Autowired
    WeatherRepository weatherRepository;

    @Autowired
    protected TestRestTemplate testRestTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @LocalServerPort
    protected int port;

    private MockRestServiceServer mockServer;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void createWeatherWithValidCityWhenExternalApiReturnsValidResponseThenReturnSavedWeather() throws URISyntaxException, JsonProcessingException {
        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI("/weather?q=Bucharest&APPID=testKey")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(getWeatherResponse(CITY)))
                );


        final ResponseEntity<WeatherResponseDto> responseEntity =
                testRestTemplate.postForEntity(constructBaseUrl().queryParam("city", CITY).build().toUriString(), null, WeatherResponseDto.class);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(CITY, responseEntity.getBody().getCity());
        assertEquals(1, weatherRepository.findAll().size());
    }

    @Test
    void createWeatherWithInvalidCityWhenExternalApiReturnsErrorThenThrowNotFoundException() throws URISyntaxException, JsonProcessingException {
        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI("/weather?q=Z&APPID=testKey")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("city not found"))
                );


        final ResponseEntity<ApiError> responseEntity =
                testRestTemplate.postForEntity(constructBaseUrl().queryParam("city", "Z").build().toUriString(), null, ApiError.class);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(NOT_FOUND, responseEntity.getBody().getCode());
        assertEquals("City not found", responseEntity.getBody().getMessage());
    }

    @Test
    void createWeatherWithMissingCityParamThenThrowBadRequestException() {
        final ResponseEntity<ApiError> responseEntity =
                testRestTemplate.postForEntity(constructBaseUrl().build().toUriString(), null, ApiError.class);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(BAD_REQUEST, responseEntity.getBody().getCode());
        assertEquals("Required String parameter 'city' is not present", responseEntity.getBody().getMessage());
    }

    protected UriComponentsBuilder constructBaseUrl() {
        return UriComponentsBuilder.newInstance().scheme(HTTP_PROTOCOL)
                .host(HOST)
                .port(getPort())
                .pathSegment(getBasePath());
    }

    protected String getBasePath() {
        return "weather";
    }

    protected int getPort() {
        return port;
    }
}
