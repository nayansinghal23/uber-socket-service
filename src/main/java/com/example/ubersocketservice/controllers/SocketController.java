package com.example.ubersocketservice.controllers;

import com.example.ubersocketservice.dtos.RideRequestMessage;
import com.example.ubersocketservice.services.SocketNotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/socket")
public class SocketController {
    private final SocketNotificationService socketNotificationService;

    public SocketController(SocketNotificationService socketNotificationService) {
        this.socketNotificationService = socketNotificationService;
    }

    @PostMapping("/notify/ride-request")
    public ResponseEntity<String> notify(@RequestBody RideRequestMessage dto) {
        try {
            for (Long driverId : dto.getNearbyDriverIds()) {
                socketNotificationService.notifyDriver(driverId, dto);
            }
            return new ResponseEntity<>("Notified drivers", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
