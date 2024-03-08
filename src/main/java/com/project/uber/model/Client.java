package com.project.uber.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "clients")
public class Client extends User {

    public Client(String name, String email, String password,
                  int phoneNumber, int taxPayerNumber, String street,
                  String city, int postalCode) {
        super(name, email, password, phoneNumber, taxPayerNumber, street,
                city, postalCode);
    }


}
