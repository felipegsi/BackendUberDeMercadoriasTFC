package com.project.uber.dtos;


import com.project.uber.enums.Category;
import com.project.uber.enums.PaymentMethod;
import com.project.uber.model.Client;
import com.project.uber.model.Driver;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;



@Getter
@Setter
public class OrderDto {
    private String origin;
    private String destination;
    private String description;
    private String feedback;
    private Category category;
    private int width;
    private int height;
    private int length;
    private float weight;//peso


    public OrderDto() {
    }

    public OrderDto(String origin, String destination, String description,
                    String feedback, Category category, int width, int height,
                    int length, float weight) {
        this.origin = origin;
        this.destination = destination;
        this.description = description;
        this.feedback = feedback;
        this.category = category;
        this.width = width;
        this.height = height;
        this.length = length;
        this.weight = weight;
    }

    public OrderDto(String origin, String destination, String description, String feedback, Category category) {
        this.origin = origin;
        this.destination = destination;
        this.description = description;
        this.feedback = feedback;
        this.category = category;
    }
}
