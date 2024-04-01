package com.project.uber.service.implementation;

import com.project.uber.dtos.DriverDto;
import com.project.uber.dtos.VehicleDto;
import com.project.uber.infra.exceptions.BusinessException;
import com.project.uber.model.Driver;
import com.project.uber.repository.DriverRepository;
import com.project.uber.repository.VehicleRepository;
import com.project.uber.service.interfac.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.project.uber.model.Vehicle;

import java.util.List;

@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private VehicleRepository vehicleRepository;


    @Override
    public DriverDto saveDriver(DriverDto driverDto) {
        // Verifica se o motorista já existe
        Driver driverAlreadyExists = driverRepository.findByEmail(driverDto.email());
        if (driverAlreadyExists != null) {
            throw new BusinessException("Driver already exists!");
        }

        // Codifica a senha
        var passwordHash = passwordEncoder.encode(driverDto.password());

        // Cria uma nova entidade Driver e a salva
        Driver newDriver = new Driver(
                driverDto.name(),
                driverDto.email(),
                passwordHash,
                driverDto.phoneNumber(),
                driverDto.taxPayerNumber(),
                driverDto.street(),
                driverDto.city(),
                driverDto.postalCode()
        );

        // Salva o motorista no repositório
        newDriver = driverRepository.save(newDriver);


        // Se informações do veículo estiverem presentes, cria e salva o veículo
        Vehicle vehicleDto = driverDto.vehicleDto();
        if (vehicleDto != null) {
            Vehicle vehicle = new Vehicle();
            vehicle.setDriver(newDriver); // Associa o veículo ao novo motorista
            vehicle.setYear(vehicleDto.getYear());
            vehicle.setPlate(vehicleDto.getPlate());
            vehicle.setBrand(vehicleDto.getBrand());
            vehicle.setModel(vehicleDto.getModel());

            vehicleRepository.save(vehicle);

        }

        // Retorna o DriverDto atualizado
        return new DriverDto(
                newDriver.getName(),
                newDriver.getEmail(),
                newDriver.getPassword(),
                newDriver.getPhoneNumber(),
                newDriver.getTaxPayerNumber(),
                newDriver.getStreet(),
                newDriver.getCity(),
                newDriver.getPostalCode(),
                vehicleDto // Aqui você pode precisar de um construtor atualizado ou um método setter
        );
    }
    private VehicleDto convertToDto(Vehicle vehicle) {
        // Implementa a lógica para converter uma entidade Vehicle para VehicleDto
        return new VehicleDto(
                vehicle.getYear(),
                vehicle.getPlate(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getDocumentPhoto()
        );
    }

    @Override
    public void setDriverOnlineStatus(Long driverId, boolean isOnline) throws Exception {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new Exception("Driver not found."));
        driver.setOnline(isOnline);
        driverRepository.save(driver);
    }

    @Override
    public void deleteDriver(Long driverId) {
        driverRepository.deleteById(driverId);
    }

    @Override
    public Driver getDriverById(Long driverId) {
        return driverRepository.findById(driverId).orElseThrow(() -> new BusinessException("Driver not found"));
    }

    @Override
    public Driver getDriverByEmail(String email) {
        Driver driver = driverRepository.findByEmail(email);
        if (driver == null) {
            throw new BusinessException("Driver not found");
        }
        return driver;
    }

    @Override
    public DriverDto viewProfile(Long driverId) {
        Driver driver = getDriverById(driverId);
        return new DriverDto(driver.getName(), driver.getEmail(), driver.getPassword(),
                driver.getPhoneNumber(), driver.getTaxPayerNumber(), driver.getStreet(),
                driver.getCity(), driver.getPostalCode(), driver.getVehicle()
        );
    }

    @Override
    public DriverDto editProfile(Long driverId, DriverDto driverDto) {

        Driver driver = getDriverById(driverId);

        driver.setName(driverDto.name());
        driver.setEmail(driverDto.email());
        driver.setPhoneNumber(driverDto.phoneNumber());
        driver.setTaxPayerNumber(driverDto.taxPayerNumber());
        driver.setStreet(driverDto.street());
        driver.setCity(driverDto.city());
        driver.setPostalCode(driverDto.postalCode());
        //driver.setCriminalRecord(driverDto.criminalRecord());

        driverRepository.save(driver);

        return new DriverDto(driver.getName(), driver.getEmail(), driver.getPassword(),
                driver.getPhoneNumber(), driver.getTaxPayerNumber(), driver.getStreet(),
                driver.getCity(), driver.getPostalCode(), driver.getVehicle()
        );
    }
    @Override
    public void acceptOrder(Long orderId, Long driverId, String driverEmail) throws BusinessException {
        Driver driver = driverRepository.findById(driverId).orElseThrow(() -> new BusinessException("Driver not found"));
        driver.setId(orderId);
        driverRepository.save(driver);
    }

}

