package com.project.uber.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "clients")
@Data
public class Client extends User {

    //adicionar aqui a lista de metodo de pagamento
    //adicionar aqui a lista de encomendas
//outras variaveis , PAREI AQUI
}