package com.project.uber.service.implementation;

import com.project.uber.dtos.DriverDto;
import com.project.uber.dtos.OrderDto;
import com.project.uber.enums.OrderStatus;
import com.project.uber.infra.exceptions.BusinessException;
import com.project.uber.model.Driver;
import com.project.uber.model.Order;
import com.project.uber.repository.DriverRepository;
import com.project.uber.repository.OrderRepository;
import com.project.uber.service.interfac.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public DriverDto saveDriver(DriverDto driverDto) {

        Optional<Driver> driverAlreadyExists = driverRepository.findByEmail(driverDto.email());

        if (driverAlreadyExists.isPresent()) {
            throw new BusinessException("Driver already exists!");
        }

        var passwordHash = passwordEncoder.encode(driverDto.password());

        Driver entity = new Driver(driverDto.name(), driverDto.email(), passwordHash,
                driverDto.phoneNumber(), driverDto.taxPayerNumber(), driverDto.street(),
                driverDto.city(), driverDto.postalCode(), driverDto.criminalRecord(), driverDto.vehicles());

        Driver newDriver = driverRepository.save(entity);

        return new DriverDto(newDriver.getName(), newDriver.getEmail(), null, // A senha não deve ser retornada
                newDriver.getPhoneNumber(), newDriver.getTaxPayerNumber(), newDriver.getStreet(),
                newDriver.getCity(), newDriver.getPostalCode(), newDriver.getCriminalRecord(),newDriver.getVehicles());
    }
    @Override
    public void acceptOrder(Long orderId, Long driverId, String driverEmail) throws BusinessException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("Pedido não encontrado"));

        if (!order.getStatus().equals(OrderStatus.PENDING)) {
            throw new BusinessException("Pedido não está pendente");
        }

        Driver driver = driverRepository.findByEmail(driverEmail)
                .orElseThrow(() -> new BusinessException("Motorista não encontrado"));

        order.setDriver(driver);
        order.setStatus(OrderStatus.ACCEPTED);
        orderRepository.save(order);
    }
    @Override
    public List<OrderDto> getOrderHistory(Long driverId) {
        List<Order> orders = orderRepository.findByClientId(driverId);
        System.out.println("\n\n\n\n\n\norders = " + orders);
        System.out.println("driverId = " + driverId);
        System.out.println(
                "orders.stream().map(this::convertToOrderDto).collect(Collectors.toList()) = " + orders.stream().map(this::convertToOrderDto).collect(Collectors.toList())
        );
        System.out.println("entrou aqui\n\n\n\n\n\n");
        return orders.stream()
                .map(this::convertToOrderDto)
                .collect(Collectors.toList());
    }
    private OrderDto convertToOrderDto(Order order) {
        return new OrderDto(
                order.getOrigin(),
                order.getDestination(),
                order.getValue(),
                order.getDescription(),
                order.getFeedback()
        );
    }
}

