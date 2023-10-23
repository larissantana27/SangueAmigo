package com.example.SangueAmigo.model.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

@Table(name = "biological_information")
@Entity(name = "BiologicalInformation")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class BiologicalInformation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "America/Sao_Paulo")
    private Date birthday;
    @Enumerated(EnumType.STRING)
    private BiologicalSex biologicalSex;
    @Enumerated(EnumType.STRING)
    private BloodType bloodType;
    private boolean rhFactor;
    private BigDecimal weight;

    public BiologicalInformation(Date birthday, BiologicalSex biologicalSex,
                                 BloodType bloodType, boolean rhFactor, BigDecimal weight) {
        this.birthday = birthday;
        this.biologicalSex = biologicalSex;
        this.bloodType = bloodType;
        this.rhFactor = rhFactor;
        this.weight = weight;
    }
}
