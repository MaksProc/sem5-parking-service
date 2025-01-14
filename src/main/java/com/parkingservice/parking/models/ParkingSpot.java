package com.parkingservice.parking.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "parking_spots")
public class ParkingSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parking_lot_id", nullable = false)
    @JsonBackReference
    private ParkingLot parkingLot;

    @Column(name = "spot_number", nullable = false)
    @JsonBackReference
    private String spotNumber;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;

    @OneToMany(mappedBy = "parkingSpot")
    @JsonBackReference
    private List<Reservation> reservations;

}