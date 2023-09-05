package com.example.transback;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketServerHandler {

    @MessageMapping("/upload-progress")
    @SendTo("/topic/upload-progress")
    public String handleUploadProgress(String progress) {
        return progress;
    }
}
