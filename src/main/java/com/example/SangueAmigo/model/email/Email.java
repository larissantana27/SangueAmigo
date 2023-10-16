package com.example.SangueAmigo.model.email;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Table(name = "email")
@Entity(name = "email")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Email implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String ownerRef;
    private String emailFrom;
    private String emailTo;
    private String subject;
    @Column(columnDefinition = "TEXT")
    private String text;
    private LocalDateTime sendDate;
    private EmailStatus emailStatus;

    public Email (String ownerRef, String emailTo, String subject, String text) {
        this.ownerRef = ownerRef;
        this.emailTo = emailTo;
        this.subject = subject;
        this.text = text;
    }
}