package com.example.SangueAmigo.infrastructure.controlers;

import com.example.SangueAmigo.model.email.Email;
import com.example.SangueAmigo.service.EmailService;
import com.example.SangueAmigo.service.security.TokenService;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public class EmailController {

    private final Logger logger = LoggerFactory.getLogger(EmailController.class);

    @Autowired
    private TokenService tokenService;
    @Autowired
    EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<Email> sendEmail(@NonNull HttpServletRequest request) {
        logger.info("-Starting Sending Email-");

        String subject = "Notificação Sangue Amigo";
        String body = "Saudações!\n\n" +
                        "Você está recebendo esse Email porque habilitou a opção de notificação do SangueAmigo, " +
                            "site para incentivo e facilitação da doação de sangue.\n" +
                        "Futuramente, você receberá avisos de quando já está apto a doar novamente e " +
                            "alertas de estados críticos de estoque para o seu tipo sanguíneo.\n\n" +
                        "Até breve.\n\n" +
                        "Larissa Duarte Santana\n" +
                        "Representante da Equipe Sangue Amigo.";

        String token = tokenService.recoverToken(request);
        String userEmail = tokenService.getEmailFromToken(token);

        Email email = new Email("Larissa", userEmail, subject, body);
        logger.info("Email: {}", email);
        emailService.sendEmail(email);

        logger.info("-Email Sent Successfully-");

        return new ResponseEntity<>(email, HttpStatus.CREATED);
    }
}