package com.parkingservice.parking.controllers.dto;

import com.parkingservice.parking.models.ReservationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationDTO {
    private Long userID;
    private Long parkingSpotID;
    private LocalDateTime endTime;

}
