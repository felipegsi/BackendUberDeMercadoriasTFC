package com.project.uber.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "drivers")
@Data
public class Driver extends User {
    @Lob
    @Column(name = "criminalRecord")
    private byte[] criminalRecord; // Confirmado como byte[] para armazenar uma imagem

    @Column(name = "salary")
    private double salary; // Alterado para BigDecimal

    @Column(name = "availability")
    private Boolean availability;

    @OneToMany(mappedBy = "driver")
    private List<Vehicle> vehicles; // Corrigido o mapeamento


    public Driver(String name, String email, String password, int phoneNumber, int taxPayerNumber, String street, String city, int postalCode, byte[] criminalRecord,List<Vehicle> vehicles) {
        super(name, email, password, phoneNumber, taxPayerNumber, street, city, postalCode);
        this.criminalRecord = criminalRecord;
        this.vehicles = vehicles;
    }

    public Driver() {

    }
}
