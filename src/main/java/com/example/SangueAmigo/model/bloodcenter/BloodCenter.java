package com.example.SangueAmigo.model.bloodcenter;

import com.example.SangueAmigo.model.user.AddressInformation;
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
    private Long id;
    private Long logoId;
    private AddressInformation addressInformation;
    private String operatingTime;
    private int phoneNumber;
    private String website;

//    private BloodStock BloodStock;
}
