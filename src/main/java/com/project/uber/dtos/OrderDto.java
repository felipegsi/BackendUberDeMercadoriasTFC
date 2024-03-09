package com.project.uber.dtos;


import java.math.BigDecimal;
        import java.time.LocalDate;
        import java.time.LocalTime;

public record OrderDto(
        String origin,
        String destination,
        BigDecimal value,
        String description,
        String feedback
) {}

