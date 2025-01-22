package com.parkingservice.parking.services;

import com.parkingservice.parking.config.WeatherServiceProperties;
import com.parkingservice.parking.controllers.ParkingLotController;
import com.parkingservice.parking.controllers.dto.WeatherDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WeatherService {

    private final WebClient webClient;
    private static final Logger logger = LogManager.getLogger(WeatherService.class);

    public WeatherService(WebClient.Builder webClientBuilder, WeatherServiceProperties properties) {
        String baseUrl = properties.getUrl();
        if (baseUrl == null || baseUrl.isEmpty()) {
            throw new IllegalStateException("Base URL for WeatherService is not configured");
        }
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public WeatherDTO getWeatherForCity(String city) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("")
                        .queryParam("city", city)
                        .build())
                .retrieve()
                .bodyToMono(WeatherDTO.class)
                .block();

    }
}
