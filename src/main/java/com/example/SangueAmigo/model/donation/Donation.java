package com.example.SangueAmigo.model.donation;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Table(name = "donation")
@Entity(name = "Donation")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Donation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String bloodCenter;
    @DateTimeFormat
    private Date date;
    private Long attendanceDocId;
    //TODO: See how this correlation will work - private Long userId;
}
