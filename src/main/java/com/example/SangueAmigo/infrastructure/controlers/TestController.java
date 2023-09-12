package com.example.SangueAmigo.infrastructure.controlers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    // TODO: Make sure the access is being controlled by the tokens in the right way,
    //  up until now I could only make everyone or anyone access this methods
    @GetMapping("/all")
    public String allAccess() {
        return "PUBLIC CONTENT";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('user') or hasRole('admin')")
    public String userAccess() {
        return "USERS CONTENT";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('admin')")
    public String adminAccess() {
        return "ONLY ADMIN CAN SEE THIS";
    }
}