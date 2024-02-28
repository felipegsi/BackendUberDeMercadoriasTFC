package com.project.uber.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "vehicles")
@Data
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @Column(name = "year")
    private int year;

    @Column(name = "plate", length = 10) // Ajustado para String e definido um tamanho máximo
    private String plate;

    @Column(name = "brand", length = 50) // Ajustado para String e definido um tamanho máximo
    private String brand;

    @Column(name = "model", length = 50) // Ajustado para String e definido um tamanho máximo
    private String model;

    @Lob
    @Column(name = "document_photo")
    private byte[] documentPhoto; // Campo adicionado para armazenar a foto do documento do veículo
}
