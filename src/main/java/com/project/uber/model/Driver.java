package com.project.uber.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "drivers")
@Data
public class Driver extends User {
    //@Lob
   // @Column(name = "criminalRecord")//estava dando erro aqui por causa do tipo de dado
    // private byte[] criminalRecord; // Confirmado como byte[] para armazenar uma imagem

    @Column(name = "salary")
    private double salary; // Alterado para BigDecimal

    @Column(name = "is_online")
    private boolean isOnline = false; // Offline por padrão

    @OneToOne(mappedBy = "driver")
   private Vehicle vehicle; // Corrigido o mapeamento


    public Driver(String name, String email, String password,
                  String phoneNumber, int taxPayerNumber, String street,
                  String city, int postalCode,
                  //byte[] criminalRecord
                  Vehicle vehicle
                  ) {
        super(name, email, password, phoneNumber, taxPayerNumber, street,
                city, postalCode);
        //this.criminalRecord = criminalRecord;
       this.vehicle = vehicle;
    }

    public Driver() {

    }


    public Driver(String name, String email, String passwordHash, String s, int i, String street, String city, int i1) {
        super(name, email, passwordHash, s, i, street,
                city, i1);
    }
}
