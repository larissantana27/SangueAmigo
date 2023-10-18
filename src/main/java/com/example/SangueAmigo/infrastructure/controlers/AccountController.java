package com.example.SangueAmigo.infrastructure.controlers;

import com.example.SangueAmigo.infrastructure.DTOs.DonationDTO;
import com.example.SangueAmigo.model.donation.Donation;
import com.example.SangueAmigo.model.donation.DonationRepository;
import com.example.SangueAmigo.model.user.User;
import com.example.SangueAmigo.model.user.UserRepository;
import com.example.SangueAmigo.service.AccountService;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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

    @GetMapping("/{userId}/lastDonationDate")
    public ResponseEntity<Date> getLastDonationDate(@NonNull HttpServletRequest request) {
        int userId = 0;
        try {
            String token = tokenService.recoverToken(request);
            if (token != null) {
                String email = tokenService.validateToken(token);
                User user = (User) userRepository.findByEmail(email);
                userId = user.getId();
            }
            Date lastDonationDate = donationRepository.findLastDonationDateByUserId(userId);
            return ResponseEntity.ok(getFormattedDate(lastDonationDate));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{userId}/nextDonationDate")
    public ResponseEntity<Date> getNextDonationDate(@NonNull HttpServletRequest request) {
        int userId = 0;
        try {
            String token = tokenService.recoverToken(request);
            if (token != null) {
                String email = tokenService.validateToken(token);
                User user = (User) userRepository.findByEmail(email);
                userId = user.getId();
            }
            Date lastDonationDate = donationRepository.findLastDonationDateByUserId(userId);
            Date nextDonationDate = plusMonths(lastDonationDate);
            return ResponseEntity.ok(getFormattedDate(nextDonationDate));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    private Date getFormattedDate(Date date) {
        try {
            logger.info("Database date: {}", date);
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = inputDateFormat.format(date);
            logger.info("Date string: {}", dateString);
            logger.info("Date formatted: {}", inputDateFormat.parse(dateString));
            return inputDateFormat.parse(dateString);
        } catch (ParseException e) {
            logger.error("Error formatting data: {}", e.getMessage(), e);
            return null;
        }
    }

    public Date plusMonths (Date lastDonationDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(lastDonationDate);
        calendar.add(Calendar.MONTH, 3);
        return calendar.getTime();
    }

    @GetMapping("/{userId}/donationQuantity")
    public ResponseEntity<Integer> getDonationQuantity(@NonNull HttpServletRequest request) {
        int userId = 0;
        try {
            String token = tokenService.recoverToken(request);
            if (token != null) {
                String email = tokenService.validateToken(token);
                User user = (User) userRepository.findByEmail(email);
                userId = user.getId();
            }
            int donationQuantity = donationRepository.getDonationQuantityByUserId(userId);
            return ResponseEntity.ok(donationQuantity);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/donation")
    public ResponseEntity<Void> donation(@NonNull HttpServletRequest request,
                                         @RequestPart("data") DonationDTO data,
                                         @RequestPart("file") MultipartFile file) {
        logger.info("-Starting Donation Post-");

        String bloodCenter = data.bloodCenter();
        Date date = putFormattedDate(data.date());
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

    private Date putFormattedDate(Date date) {
        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = inputDateFormat.format(date);

            SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            outputDateFormat.setTimeZone(TimeZone.getTimeZone("America/Recife"));

            String formattedDateString = dateString + " 00:00:00";
            Date formattedDate = outputDateFormat.parse(formattedDateString);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(formattedDate);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            return calendar.getTime();
        } catch (ParseException e) {
            logger.error("Error formatting data: {}", e.getMessage(), e);
            return null;
        }
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