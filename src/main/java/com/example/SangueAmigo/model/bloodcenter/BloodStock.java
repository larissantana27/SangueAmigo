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
public class BloodStock {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private Long id;
    private String bloodCenter; //or BloodCenter
    private String type;
    private Float quantity;
}
