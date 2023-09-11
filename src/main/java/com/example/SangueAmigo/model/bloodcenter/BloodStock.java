package com.example.SangueAmigo.model.bloodcenter;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "blood_stock")
@Entity(name = "BloodStock")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class BloodStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //TODO: Decide type String or BloodCenter
    private String bloodCenter;
    private String type;
    private Float quantity;
}
