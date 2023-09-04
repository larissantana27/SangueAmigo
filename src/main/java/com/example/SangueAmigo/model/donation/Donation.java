package com.example.SangueAmigo.model.donation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Table(name = "donation")
@Entity(name = "Donation")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Donation {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private Long id;
    private String bloodCenter;
    private DateTimeFormat date;
    private Long attendanceDocId;

//    private Long userId;
}
