package com.parkingservice.parking.controllers;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/session")
public class SessionController {

    private final SimpMessageSendingOperations messagingTemplate;

    public SessionController(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }


    private String generateSessionReport() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = sdf.format(new Date());
        String sessionId = "Session123";
        String status = "active";

        return String.format("Session ID: %s, Status: %s, Timestamp: %s", sessionId, status, timestamp);
    }


    @GetMapping("/report")
    public String sendSessionReport(@RequestParam String sessionId) {

        String report = generateSessionReport();
        messagingTemplate.convertAndSend("/topic/sessionStatus", report);
        return "Session report sent: " + report;
    }

}