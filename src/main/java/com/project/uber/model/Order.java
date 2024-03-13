package com.project.uber.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.uber.enums.OrderStatus;
import com.project.uber.enums.PaymentMethod; // Certifique-se de que este enum está definido
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "orders") // Prefira usar um padrão consistente para nomes de tabelas e colunas, seja snake_case ou camelCase
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String origin;

    @Column(nullable = false, length = 150)
    private String destination;

    @Column(nullable = false, precision = 10, scale = 2) // Ajuste precision e scale conforme necessário
    private BigDecimal value; // BigDecimal é adequado para valores monetários

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // Supondo a existência de um enum OrderStatus

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDate date; // Data de criação

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalTime time; // Hora de criação

    @Column(length = 500) // Ajuste o tamanho conforme necessário
    private String description;

    @Column(length = 1000) // Permita um feedback mais longo, se necessário
    private String feedback;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "client_id") // Chave estrangeira na tabela 'orders'
    private Client client;

    // Campos adicionados conforme sugerido
    @Enumerated(EnumType.STRING)
    private PaymentMethod payment; // Supondo que você tenha um enum PaymentMethod

    @ManyToOne
    @JoinColumn(name = "driver_id") // Define a coluna de chave estrangeira para Driver
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = "vehicle_id") // Define a coluna de chave estrangeira para Vehicle
    private Vehicle vehicle;

    @Column(nullable = true) // Avaliações podem não estar presentes imediatamente
    private Integer rating; // Considerando rating como um número
}
