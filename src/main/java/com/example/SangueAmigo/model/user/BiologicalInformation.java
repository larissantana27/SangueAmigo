package com.example.SangueAmigo.model.user;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.text.DecimalFormat;

enum BiologicalSex {
    male, female
}

enum BloodType {
    A, B, AB, O
}

@Table(name = "biological_information")
@Entity(name = "BiologicalInformation")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class BiologicalInformation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Date birthday;
    private BiologicalSex biologicalSex;
    private BloodType bloodType;
    private boolean rhFactor;
    private DecimalFormat weight;
    //TODO: See how this correlation will work - private Long userId;
}
