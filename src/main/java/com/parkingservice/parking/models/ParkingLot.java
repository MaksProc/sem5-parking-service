package com.parkingservice.parking.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "parking_lots")
public class ParkingLot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;


    @Column(nullable = false)
    private String address;

    @Column(name = "total_spots", nullable = false)
    private Integer totalSpots;

    @OneToMany(mappedBy = "parkingLot")
    @JsonManagedReference
    private List<ParkingSpot> parkingSpots;

}