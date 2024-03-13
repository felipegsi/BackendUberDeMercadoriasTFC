package com.project.uber.service.interfac;

import com.project.uber.dtos.ClientDto;
import com.project.uber.dtos.OrderDto;
import com.project.uber.model.Order;

import java.util.List;

public interface ClientService {

    public ClientDto saveClient(ClientDto clientDto);

    public Order createOrder(OrderDto orderDto, Long clientId);

    public List<OrderDto> getOrderHistory(Long clientId);

    public void deleteClient(Long clientId);

    public void changePassword(String email, String newPassword);

    //quais outras funcionalidades o cliente pode ter?
    //criar um pedido - semi-implementado
    //verificar o histórico de pedidos - semi-implementado
    //deletar a conta - semi-implementado
    //mudar a senha - nao-implementado
    //recuperar a senha
    //atualizar os dados
    //verificar os dados
    //verificar os pedidos


}
