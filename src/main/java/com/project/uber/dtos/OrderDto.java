package com.project.uber.dtos;


import java.math.BigDecimal;

public record OrderDto(
        String origin,
        String destination,
        BigDecimal value,
        String description,
        String feedback
) {}

