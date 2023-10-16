package com.example.SangueAmigo.infrastructure.controlers;

import com.example.SangueAmigo.infrastructure.DTOs.EmailDTO;
import com.example.SangueAmigo.model.email.Email;
import com.example.SangueAmigo.service.EmailService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public class EmailController {

    private final Logger logger = LoggerFactory.getLogger(EmailController.class);

    @Autowired
    EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<Email> sendEmail(@RequestBody @Valid EmailDTO emailDto) {
        logger.info("-Starting Sending Email-");

        Email email = new Email();
        BeanUtils.copyProperties(emailDto, email);
        logger.info("Email: {}", email);
        emailService.sendEmail(email);

        logger.info("-Email Sent Successfully-");

        return new ResponseEntity<>(email, HttpStatus.CREATED);
    }
}