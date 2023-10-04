package com.example.SangueAmigo.model.bloodcenter;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Table(name = "blood_stock")
@Entity(name = "BloodStock")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class BloodStock {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String type;
    private Float quantity;
    private Date date;
    private int bloodCenterId;
}
