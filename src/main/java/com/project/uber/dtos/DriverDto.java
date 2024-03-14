package com.project.uber.dtos;

import com.project.uber.model.Vehicle;

import java.util.List;

public record DriverDto(String name,
                        String email,
                        String password,
                        String phoneNumber,
                        int taxPayerNumber,
                        String street,
                        String city,
                        int postalCode,
                        byte[] criminalRecord,
                        List<Vehicle> vehicles) { }
