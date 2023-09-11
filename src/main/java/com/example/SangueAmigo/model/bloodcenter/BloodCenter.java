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

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private int id;
    private Long logoId;
    private Long addressInformationId;
    private String operatingTime;
    private int phoneNumber;
    private String website;
    // TODO: ADD private BloodStock BloodStock;
}
