package com.project.uber.dtos;


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

    public OrderDto(String origin, String destination, String description, String feedback) {
        this.origin = origin;
        this.destination = destination;
        this.description = description;
        this.feedback = feedback;
    }


}
