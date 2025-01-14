package com.parkingservice.parking.controllers.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingSpotDTO {
    private String spotNumber;
    private Boolean isAvailable;
    private Long parkingLotID;
}
