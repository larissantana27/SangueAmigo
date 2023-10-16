package com.example.SangueAmigo.service;

import com.example.SangueAmigo.model.email.Email;
import com.example.SangueAmigo.model.email.EmailRepository;
import com.example.SangueAmigo.model.email.EmailStatus;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    EmailRepository emailRepository;
    @Autowired
    private JavaMailSender emailSender;

    @Transactional
    public void sendEmail(Email email) {
        email.setSendDate(LocalDateTime.now());
        email.setEmailFrom(fromEmail);
        try{
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(fromEmail);
            message.setTo(email.getEmailTo());
            message.setSubject(email.getSubject());
            message.setText(email.getText());

            emailSender.send(message);

            email.setEmailStatus(EmailStatus.SENT);
        } catch (MailException e){
            email.setEmailStatus(EmailStatus.ERROR);
        } finally {
            emailRepository.save(email);
        }
    }
}