package com.example.ubersocketservice.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RideRequestMessage {
    private Long bookingId;
    private Long passengerId;
    private Double pickupLatitude;
    private Double pickupLongitude;
    private Double dropLatitude;
    private Double dropLongitude;
    private List<Long> nearbyDriverIds;
}