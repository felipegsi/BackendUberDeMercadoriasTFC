package com.project.uber.service.implementation;

import com.project.uber.dtos.OrderDto;
import com.project.uber.enums.OrderStatus;
import com.project.uber.model.Client;
import com.project.uber.model.Order;
import com.project.uber.repository.OrderRepository;
import com.project.uber.service.interfac.ClientService;
import com.project.uber.service.interfac.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ClientService clientService;

    @Override
    public OrderDto saveOrder(OrderDto orderDto, Long clientId) {
//entra  uma orderDto e sai uma orderDto porem no meio disso
// a Order é salva no banco de dados

        BigDecimal value = BigDecimal.valueOf(10.70);
        OrderStatus status = OrderStatus.PENDING;
        Client client = clientService.getClientById(clientId);

        Order order = new Order(orderDto.origin(), orderDto.destination(),
                value, status, LocalDate.now(), LocalTime.now(),
                orderDto.description(), orderDto.feedback(), client);

        orderRepository.save(order);

        return new OrderDto(order.getOrigin(), order.getDestination(), order.getDescription(), order.getFeedback());

    }

    @Override
    public List<OrderDto> getClientOrderHistory(Long clientId) {
        // Localize todos os pedidos do cliente pelo ID do cliente
        List<Order> orders = orderRepository.findByClientId(clientId);

        return orders.stream()
                .map(this::convertToOrderDto)
                .collect(Collectors.toList());
    }

    private OrderDto convertToOrderDto(Order order) {
        // Implemente a conversão aqui
        return new OrderDto(
                order.getOrigin(),
                order.getDestination(),
               // order.getValue(),
                order.getDescription(),
                order.getFeedback()

        );
    }

}
