package com.example.SangueAmigo.model.donation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Blob;
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
    private Blob attendanceDoc;
    private int user_id;

    public Donation (String bloodCenter, Date date, Blob attendanceDoc, int user_id) {
        this.bloodCenter = bloodCenter;
        this.date = date;
        this.attendanceDoc = attendanceDoc;
        this.user_id = user_id;
    }
}
