package com.project.uber.service.interfac;

import com.project.uber.dtos.ClientDto;
import com.project.uber.dtos.DriverDto;
import com.project.uber.dtos.OrderDto;
import com.project.uber.infra.exceptions.BusinessException;
import com.project.uber.model.Order;

import java.util.List;

public interface DriverService {

    public DriverDto saveDriver(DriverDto driverDto);
    public List<OrderDto> getOrderHistory(Long clientId);
    void acceptOrder(Long orderId, Long driverId, String driverEmail) throws BusinessException;
}
