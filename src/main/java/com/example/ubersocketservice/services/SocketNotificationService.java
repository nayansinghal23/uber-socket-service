package com.example.ubersocketservice.services;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class SocketNotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    public SocketNotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyDriver(Long driverId, Object payload) {
        messagingTemplate.convertAndSend(
                "/topic/driver/" + driverId,
                payload
        );
    }

    public void notifyPassenger(Long bookingId, Object payload) {
        messagingTemplate.convertAndSend(
                "/topic/booking/" + bookingId,
                payload
        );
    }
}
