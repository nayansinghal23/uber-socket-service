package com.example.ubersocketservice.services;

import com.example.ubersocketservice.dtos.DriverRideRequestDTO;
import com.example.ubersocketservice.dtos.RideRequestMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class SocketNotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    public SocketNotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyDriver(Long driverId, RideRequestMessage dto) {
        DriverRideRequestDTO message = DriverRideRequestDTO.builder()
                .bookingId(dto.getBookingId())
                .pickupLatitude(dto.getPickupLatitude())
                .pickupLongitude(dto.getPickupLongitude())
                .dropLatitude(dto.getDropLatitude())
                .dropLongitude(dto.getDropLongitude())
                .build();

        messagingTemplate.convertAndSend(
                "/topic/driver/" + driverId,
                message
        );
    }

    public void notifyPassenger(Long bookingId, Object payload) {
        messagingTemplate.convertAndSend(
                "/topic/booking/" + bookingId,
                payload
        );
    }
}
