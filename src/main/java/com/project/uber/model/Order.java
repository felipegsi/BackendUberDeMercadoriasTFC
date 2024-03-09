package com.project.uber.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.uber.enums.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "orders") // Use snake_case ou camelCase consistentemente em nomes de tabelas e colunas conforme a convenção do seu BD
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String origin;

    @Column(nullable = false, length = 150)
    private String destination;

    @Column(nullable = false, precision = 10, scale = 2) // precision e scale podem ser ajustados conforme a necessidade
    private BigDecimal value; // Use BigDecimal para valores monetários

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // Supondo que você tem um enum OrderStatus

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDate date; // Data de criação

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalTime time; // Hora de criação

    @Column(length = 500) // Tamanho ajustado conforme a necessidade de armazenamento
    private String description;

    @Column(length = 1000) // Permita um feedback mais longo se necessário
    private String feedback;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "client_id")// Define a coluna de chave estrangeira na tabela 'orders'
    private Client client;

}
    /*
    @Enumerated(EnumType.STRING)
    private PaymentMethod payment; // Supondo que você tem um enum PaymentMethod

    @ManyToOne
    private Driver driver;

    @ManyToOne
    private Vehicle vehicle;

    private Integer rating; // Considerando que o rating é um número


    */


