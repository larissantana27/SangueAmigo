package com.example.SangueAmigo.infrastructure.controlers;

import com.example.SangueAmigo.model.user.UserRepository;
import com.example.SangueAmigo.infrastructure.DTOs.AuthenticationDTO;
import com.example.SangueAmigo.infrastructure.DTOs.LoginResponseDTO;
import com.example.SangueAmigo.infrastructure.DTOs.RegisterDTO;
import com.example.SangueAmigo.model.user.User;
import com.example.SangueAmigo.service.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository repository;

    @Autowired
    private TokenService tokenService;

    @GetMapping("/seeUser") // Temporary Method
    public ResponseEntity<UserDetails> seeUser(@RequestBody @Valid AuthenticationDTO data) {
       UserDetails userDetails = repository.findByEmail(data.email());
       return ResponseEntity.ok(userDetails);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterDTO data) {
        logger.info("-Starting User Register-");

        if (this.repository.findByEmail(data.email()) != null) {
            logger.info("E-mail already registered. Register Aborted.");
            return ResponseEntity.badRequest().build();
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.firstname(), data.lastname(), data.email(), encryptedPassword, data.role());

        newUser = this.repository.save(newUser);

        logger.info("User ID: {}", newUser.getId());

        logger.info("-Success Registering User-");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) {
        logger.info("-Starting User Login-");

        var authenticationValues = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        logger.info("Authentication Values: principal - {} and credential - {}",
                authenticationValues.getPrincipal(), authenticationValues.getCredentials());

        var auth = this.authenticationManager.authenticate(authenticationValues);
        logger.info("Auth: {}", auth);

        var token = tokenService.generateToken((User) auth.getPrincipal());
        logger.info("User Token: {}", token);

        logger.info("-Success Logging User-");
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}