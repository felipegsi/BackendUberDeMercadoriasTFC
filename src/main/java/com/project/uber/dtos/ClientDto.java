package com.project.uber.dtos;


public record ClientDto(
        String name,
        String email,
        String password,

        int phoneNumber,
        int taxPayerNumber,
        String street,
        String city,
        int postalCode

) {
}
