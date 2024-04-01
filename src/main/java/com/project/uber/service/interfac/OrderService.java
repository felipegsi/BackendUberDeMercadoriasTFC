package com.project.uber.service.interfac;

import com.project.uber.dtos.OrderDto;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {

    BigDecimal estimateOrderCost(OrderDto orderDto);

    public OrderDto saveOrder(OrderDto orderDto, Long clientId);
    public List<OrderDto> getClientOrderHistory(Long clientId);

}