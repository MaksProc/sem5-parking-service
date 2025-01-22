package com.parkingservice.parking.controllers.dto;

import lombok.Data;

@Data
public class WeatherDTO {
    private String description;
    private double temperature;
}