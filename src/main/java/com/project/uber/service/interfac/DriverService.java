package com.project.uber.service.interfac;

import com.project.uber.dtos.ClientDto;
import com.project.uber.dtos.DriverDto;
import com.project.uber.dtos.OrderDto;
import com.project.uber.infra.exceptions.BusinessException;
import com.project.uber.model.Client;
import com.project.uber.model.Driver;
import com.project.uber.model.Order;

import java.util.List;

public interface DriverService {

    public DriverDto saveDriver(DriverDto driverDto);

  //  void acceptOrder(Long orderId, Long driverId, String driverEmail) throws BusinessException;

    public void deleteDriver(Long driverId);

    public Driver getDriverById(Long driverId);

    public Driver getDriverByEmail(String email);

    DriverDto viewProfile(Long driverId);

    DriverDto editProfile(Long driverId, DriverDto driverDto);
}
