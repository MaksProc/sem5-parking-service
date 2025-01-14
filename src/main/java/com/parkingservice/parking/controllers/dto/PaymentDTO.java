package com.parkingservice.parking.controllers.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentDTO {
    private Long reservationID;
    private BigDecimal amount;
    private String paymentMethod;
    private String transactionID;
    private LocalDateTime paymentDate;
}
