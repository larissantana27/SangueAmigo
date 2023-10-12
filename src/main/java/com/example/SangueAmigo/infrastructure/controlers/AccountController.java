package com.example.SangueAmigo.infrastructure.controlers;

import com.example.SangueAmigo.infrastructure.DTOs.DonationDTO;
import com.example.SangueAmigo.model.donation.Donation;
import com.example.SangueAmigo.model.donation.DonationRepository;
import com.example.SangueAmigo.model.user.User;
import com.example.SangueAmigo.model.user.UserRepository;
import com.example.SangueAmigo.service.security.AccountService;
import com.example.SangueAmigo.service.security.TokenService;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DonationRepository donationRepository;
    @Autowired
    private AccountService accountService;

    @GetMapping("/list")
    public ResponseEntity<String> getAccountInfoList(@NonNull HttpServletRequest request) {
        logger.info("-Starting Account List Getter-");

        String token = tokenService.recoverToken(request);
        String userEmail = tokenService.getEmailFromToken(token);
        User user = (User) userRepository.findByEmail(userEmail);

        String result = accountService.getAccountInfoList(user.getId());

        logger.info("-Success Getting Account List-");

        return ResponseEntity.ok(result);
    }

    @PostMapping("/donation")
    public ResponseEntity<Void> donation(@NonNull HttpServletRequest request,
                                         @RequestPart("data") DonationDTO data,
                                         @RequestPart("file") MultipartFile file) {
        logger.info("-Starting Donation Post-");

        String bloodCenter = data.bloodCenter();
        Date date = data.date();
        Blob attendanceDoc = transformIntoBlob(file);
        int userId = getUserIdFromToken(request);

        logger.info("Donation bloodCenter: {}", bloodCenter);
        logger.info("Donation date: {}", date);
        logger.info("Donation attendanceDocId: {}", attendanceDoc);
        logger.info("Donation userId: {}", userId);

        Donation donation = new Donation(bloodCenter, date, attendanceDoc, userId);

        this.donationRepository.save(donation);

        logger.info("-Success Posting Donation-");
        return ResponseEntity.ok().build();
    }

    private int getUserIdFromToken (@NonNull HttpServletRequest request) {
        try {
            String token = tokenService.recoverToken(request);
            if (token != null) {
                String email = tokenService.validateToken(token);
                User user = (User) userRepository.findByEmail(email);
                return user.getId();
            }
        } catch (Exception e) {
            logger.error("It was not possible to get this user authentication:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build().getStatusCode().value();
        }
        return 0;
    }

    private Blob transformIntoBlob (@RequestPart("file") MultipartFile file) {
        try {
            byte[] fileBytes = file.getBytes();
            return new SerialBlob(fileBytes);
        } catch (IOException | SQLException e) {
            logger.error("Error processing the file: {}", e.getMessage());
            return (Blob) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}