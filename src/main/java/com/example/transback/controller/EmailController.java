package com.example.transback.controller;

import com.example.transback.dto.MailDTO;
import com.example.transback.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/email")
public class EmailController {

    private final RestTemplate restTemplate;

    @Autowired
    EmailService emailService;

    @Autowired
    public EmailController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/send")
    public String sendMail(@RequestBody MailDTO mailDto) {
        System.out.println("컨트롤러:"+mailDto);
        emailService.sendSimpleMessage(mailDto);
        System.out.println("메일 전송 완료");
        return "AfterMail.html";
    }
}
