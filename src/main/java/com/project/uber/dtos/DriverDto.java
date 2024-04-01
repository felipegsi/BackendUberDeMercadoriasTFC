package com.project.uber.dtos;

import com.project.uber.model.Vehicle;

public record DriverDto(String name,
                        String email,
                        String password,
                        String phoneNumber,
                        int taxPayerNumber,
                        String street,
                        String city,
                        int postalCode,

                        // byte[] criminalRecord evitar essa complexidade agora
                        Vehicle vehicleDto
) {
}
