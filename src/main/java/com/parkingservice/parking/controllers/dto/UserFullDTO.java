package com.parkingservice.parking.controllers.dto;

import com.parkingservice.parking.models.ERole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFullDTO {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String phoneNumber;
    private ERole role;
}
