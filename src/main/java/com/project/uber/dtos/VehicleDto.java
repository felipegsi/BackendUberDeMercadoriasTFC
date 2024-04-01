package com.project.uber.dtos;

public class VehicleDto {
    private int year;
    private String plate;
    private String brand;
    private String model;
    private byte[] documentPhoto;

    // Default constructor
    public VehicleDto() {}

    // Constructor with all fields
    public VehicleDto(int year, String plate, String brand, String model, byte[] documentPhoto) {
        this.year = year;
        this.plate = plate;
        this.brand = brand;
        this.model = model;
        this.documentPhoto = documentPhoto;
    }

    // Getters and setters
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public byte[] getDocumentPhoto() {
        return documentPhoto;
    }

    public void setDocumentPhoto(byte[] documentPhoto) {
        this.documentPhoto = documentPhoto;
    }
}
