package com.example.SangueAmigo.model.bloodcenter;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "blood_center")
@Entity(name = "BloodCenter")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class BloodCenter {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Long logo;
    private Long addressInfoId;
    private String name;
    private String operatingTime;
    private int phoneNumber;
    private String website;
}
