package com.example.SangueAmigo.infrastructure.controlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    private final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @GetMapping("/all")
    public String allAccess() {
        logger.info("-Starting All Request-");

        return "PUBLIC CONTENT";
    }

    @GetMapping("/user")
    public String userAccess() {
        logger.info("-Starting User Request-");

        return "USERS CONTENT";
    }

    @GetMapping("/admin")
    public String adminAccess() {
        logger.info("-Starting Admin Request-");

        return "ONLY ADMIN CAN SEE THIS";
    }
}