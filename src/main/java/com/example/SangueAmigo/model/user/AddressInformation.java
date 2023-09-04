package com.example.SangueAmigo.model.user;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "address_information")
@Entity(name = "AddressInformation")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class AddressInformation {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private Long id;
    private int cep;
    private String street;
    private int number;
    private String city;
    private String state;

//    private Long userId;
}
