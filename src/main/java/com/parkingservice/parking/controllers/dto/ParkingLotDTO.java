package com.parkingservice.parking.controllers.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// DTO - Data Transfer Object. Meant to be RequestBody for controller methods
// for carrying data about ParkingLot without exposing the actual entity or requiring unnecessary fields.
public class ParkingLotDTO {
    private String name;
    private String address;
    private Integer totalSpots;
}
