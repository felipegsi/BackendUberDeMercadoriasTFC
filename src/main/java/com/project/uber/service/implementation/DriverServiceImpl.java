package com.project.uber.service.implementation;

import com.project.uber.dtos.DriverDto;
import com.project.uber.infra.exceptions.BusinessException;
import com.project.uber.model.Driver;
import com.project.uber.repository.DriverRepository;
import com.project.uber.service.interfac.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public DriverDto saveDriver(DriverDto driverDto) {

        Driver driverAlredyExists = driverRepository.findByEmail(driverDto.email());

        if (driverAlredyExists != null) {
            throw new BusinessException("Driver already exists!");
        }

        var passwordHash = passwordEncoder.encode(driverDto.password());

        Driver newDriver = driverRepository.save(new Driver(driverDto.name(), driverDto.email(), passwordHash,
                driverDto.phoneNumber(), driverDto.taxPayerNumber(), driverDto.street(),
                driverDto.city(), driverDto.postalCode()
                //, driverDto.criminalRecord()
        ));//fazemos isso para nao obstruir nossa tabela de cliente e codificar a senha

        return new DriverDto(newDriver.getName(), newDriver.getEmail(), newDriver.getPassword(),
                newDriver.getPhoneNumber(), newDriver.getTaxPayerNumber(), newDriver.getStreet(),
                newDriver.getCity(), newDriver.getPostalCode()
                //        , newDriver.getCriminalRecord()
        );
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
                driver.getCity(), driver.getPostalCode()
                //        , driver.getCriminalRecord()
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
                driver.getCity(), driver.getPostalCode()
                //        , driver.getCriminalRecord()
        );


    }


}

