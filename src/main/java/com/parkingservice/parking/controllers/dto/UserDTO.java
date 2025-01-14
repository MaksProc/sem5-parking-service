package com.parkingservice.parking.controllers.dto;

import com.parkingservice.parking.models.ERole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String username;
    private ERole role;
}
