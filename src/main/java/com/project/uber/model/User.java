package com.project.uber.model;

import jakarta.persistence.*;
import lombok.Data;

@MappedSuperclass
@Data
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "phoneNumber")
    private int phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "taxPayerNumber")
    private int taxPayerNumber;

    @Column(name = "password")
    private String password;


    // Implementar a morada aqui ou criar uma classe
}