package com.example.SangueAmigo.infrastructure.controlers;

import com.example.SangueAmigo.infrastructure.DTOs.PasswordDTO;
import com.example.SangueAmigo.model.user.*;
import com.example.SangueAmigo.infrastructure.DTOs.AuthenticationDTO;
import com.example.SangueAmigo.infrastructure.DTOs.LoginResponseDTO;
import com.example.SangueAmigo.infrastructure.DTOs.RegisterDTO;
import com.example.SangueAmigo.service.security.TokenService;
import jakarta.transaction.Transactional;
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
    private UserRepository userRepository;
    @Autowired
    private BiologicalInfoRepository biologicalInfoRepository;
    @Autowired
    private AddressInfoRepository addressInfoRepository;
    @Autowired
    private TokenService tokenService;

    @GetMapping("/seeUser") // Temporary Method
    public ResponseEntity<UserDetails> seeUser(@RequestBody @Valid AuthenticationDTO data) {
       UserDetails userDetails = userRepository.findByEmail(data.email());
       return ResponseEntity.ok(userDetails);
    }

    // TODO: test if this != should be ==
    @PostMapping("/register")
    @Transactional
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterDTO data) {
        logger.info("-Starting User Register-");

        if (this.userRepository.findByEmail(data.user().getEmail()) != null) {
            logger.info("E-mail already registered. Register Aborted.");
            return ResponseEntity.badRequest().build();
        }

        logger.info("BiologicalInfo Birthday: {}", data.biologicalInfo().getBirthday());
        logger.info("BiologicalInfo Sex: {}", data.biologicalInfo().getBiologicalSex());
        logger.info("BiologicalInfo Blood Type: {}", data.biologicalInfo().getBloodType());
        logger.info("BiologicalInfo Rh Factor: {}", data.biologicalInfo().isRhFactor());
        logger.info("BiologicalInfo Weight: {}", data.biologicalInfo().getWeight());

        BiologicalInformation biologicalInfo = createBiologicalInfo(data.biologicalInfo());
        this.biologicalInfoRepository.save(biologicalInfo);
        int biologicalInfoId = biologicalInfo.getId();
        logger.info("BiologicalInfo ID: {}", biologicalInfoId);

        logger.info("AddressInfo CEP: {}", data.addressInfo().getCep());
        logger.info("AddressInfo Street: {}", data.addressInfo().getStreet());
        logger.info("AddressInfo Number: {}", data.addressInfo().getNumber());
        logger.info("AddressInfo City: {}", data.addressInfo().getCity());
        logger.info("AddressInfo State: {}", data.addressInfo().getState());

        AddressInformation addressInfo = createAddressInfo(data.addressInfo());
        this.addressInfoRepository.save(addressInfo);
        int addressInfoId = biologicalInfo.getId();
        logger.info("AddressInfo ID: {}", addressInfoId);

        User newUser =  this.userRepository.save(createUser(data.user(), biologicalInfoId, addressInfoId));

        logger.info("User ID: {}", newUser.getId());

        logger.info("-Success Registering User-");
        return ResponseEntity.ok().build();
    }

    private static BiologicalInformation createBiologicalInfo (BiologicalInformation biologicalInfo) {
        return new BiologicalInformation(biologicalInfo.getBirthday(), biologicalInfo.getBiologicalSex(),
                biologicalInfo.getBloodType(), biologicalInfo.isRhFactor(), biologicalInfo.getWeight());
    }

    private static AddressInformation createAddressInfo(AddressInformation addressInfo) {
        return new AddressInformation(addressInfo.getCep(), addressInfo.getStreet(),
                addressInfo.getNumber(), addressInfo.getCity(), addressInfo.getState());
    }

    private static User createUser(User user, int biologicalInfoId, int addressInfoId) {
        String encryptedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        return new User(user.getFirstname(), user.getLastname(), user.getEmail(),
                encryptedPassword, user.getRole(), biologicalInfoId, addressInfoId);
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

    @Transactional
    @PutMapping("/passwordSetup")
    public ResponseEntity<Void> passwordSetup(@RequestBody @Valid PasswordDTO data) {
        User user = (User) userRepository.findByEmail(data.email());

        if (user == null) {
            logger.info("E-mail not registered.");
            return ResponseEntity.badRequest().build();
        }

        String oldPassword = userRepository.getPassword(user.getId());
        logger.info("Old Password by ID: {}", oldPassword);

        String encryptedPassword = new BCryptPasswordEncoder().encode("123456");
        userRepository.setPassword(user.getId(), encryptedPassword);

        User updatedUser = (User) userRepository.findByEmail(data.email());
        String newPassword = userRepository.getPassword(updatedUser.getId());
        logger.info("New Password by ID: {}", newPassword);

        return ResponseEntity.ok().build();
    }
}