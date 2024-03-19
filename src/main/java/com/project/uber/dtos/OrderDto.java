package com.project.uber.dtos;


import com.project.uber.enums.PaymentMethod;
import com.project.uber.model.Client;
import com.project.uber.model.Driver;
import jakarta.persistence.*;

import java.math.BigDecimal;

public record OrderDto(
        String origin,
        String destination,
        //aqui leva em conta o id do client
       // BigDecimal value,//nao deve estar aqui pois o cliente nao preenche esse campo, o sistema preenche
         String description,
         String feedback

        //Client client, //nao deve estar aqui pois o cliente nao preenche esse campo, o sistema preenche
        //PaymentMethod payment //
        //Driver driver, //nao deve estar aqui pois o cliente nao preenche esse campo, o sistema preenche

) {}

