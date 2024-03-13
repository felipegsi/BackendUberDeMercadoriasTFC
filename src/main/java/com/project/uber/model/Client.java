package com.project.uber.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
//@EqualsAndHashCode(exclude = {"orders"})  //n precisa do jsonignore
@NoArgsConstructor
@Entity
@Table(name = "clients")
public class Client extends User {

    //lista de pedidos
    @JsonIgnore
    @OneToMany(mappedBy = "client")
    private List<Order> orders;


    public Client(String name, String email, String password,
                  int phoneNumber, int taxPayerNumber, String street,
                  String city, int postalCode) {
        super(name, email, password, phoneNumber, taxPayerNumber, street,
                city, postalCode);
    }

}
