package com.project.uber.service.interfac;

import com.project.uber.dtos.ClientDto;
import com.project.uber.dtos.OrderDto;
import com.project.uber.model.Order;

public interface ClientService {

    public ClientDto saveClient(ClientDto clientDto);

    public Order createOrder(OrderDto orderDto, String email);

}
