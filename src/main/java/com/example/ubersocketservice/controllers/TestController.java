package com.example.ubersocketservice.controllers;

import com.example.ubersocketservice.dtos.TestRequestDTO;
import com.example.ubersocketservice.dtos.TestResponseDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
public class TestController {
    private final SimpMessagingTemplate template;

    public TestController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/ping")
    @SendTo("/topic/ping")
    public TestResponseDTO pingCheck(TestRequestDTO dto) {
        System.out.println("Received ping request : " + dto.getData());
        return TestResponseDTO
                .builder()
                .data(dto.getData())
                .build();
    }

//    @Scheduled(fixedRate = 2000)
//    public void sendPeriodicMessage() {
//        template.convertAndSend(
//                "/topic/scheduled",
//                "Send periodic message from server " + System.currentTimeMillis()
//        );
//    }
}
