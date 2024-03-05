package com.project.uber.model;

import jakarta.persistence.*;
import lombok.Data;

@MappedSuperclass
@Data
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "phoneNumber", unique = true, nullable = false)
    private int phoneNumber;

    @Column(name = "email", unique = true, nullable = false, length = 100)//UNIQUE = true, para garantir que não haverá dois usuários com o mesmo e-mail
    private String email;

    @Column(name = "taxPayerNumber", unique = true, nullable = false)
    private int taxPayerNumber;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "street", nullable = false, length = 100)
    private String street;

    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @Column(name = "postal_code", nullable = false, length = 20)
    private int postalCode;

    @Transient //apenas fica salvo no frontend a imagem
    private byte[] profileImage;





}