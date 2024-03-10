package com.project.uber.service.implementation;

import com.project.uber.dtos.ClientDto;
import com.project.uber.dtos.OrderDto;
import com.project.uber.enums.OrderStatus;
import com.project.uber.infra.exceptions.BusinessException;
import com.project.uber.model.Client;
import com.project.uber.model.Order;
import com.project.uber.repository.ClientRepository;
import com.project.uber.repository.OrderRepository;
import com.project.uber.service.interfac.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public ClientDto saveClient(ClientDto clientDto) {

        Client clientAlredyExists = clientRepository.findByEmail(clientDto.email());

        if (clientAlredyExists != null) {
            throw new BusinessException("Client already exists!");
        }

        var passwordHash = passwordEncoder.encode(clientDto.password());

        Client entity = new Client(clientDto.name(), clientDto.email(), passwordHash,
                clientDto.phoneNumber(), clientDto.taxPayerNumber(), clientDto.street(),
                clientDto.city(), clientDto.postalCode());

        Client newClient = clientRepository.save(entity);

        return new ClientDto(newClient.getName(), newClient.getEmail(), newClient.getPassword(),
                newClient.getPhoneNumber(), newClient.getTaxPayerNumber(), newClient.getStreet(),
                newClient.getCity(), newClient.getPostalCode());
    }

    @Override
    public Order createOrder(OrderDto orderDto, String email) {
        // Localize o cliente pelo email
        Client client = clientRepository.findByEmail(email);

        if (client == null) {
            throw new BusinessException("Email não encontrado");
        }

        // Construa a nova entidade Order com os dados de OrderDto
        Order order = new Order();
        order.setOrigin(orderDto.origin());
        order.setDestination(orderDto.destination());
        order.setValue(orderDto.value());
        order.setDescription(orderDto.description());
        order.setFeedback(orderDto.feedback());

        // Defina os campos automaticamente gerados
        order.setStatus(OrderStatus.PENDING); // Supondo que todos os novos pedidos começam como PENDING
        order.setDate(LocalDate.now());       // Data atual
        order.setTime(LocalTime.now());       // Hora atual
        order.setClient(client);              // Cliente que fez o pedido

        // Salve a encomenda usando orderRepository
        return orderRepository.save(order);
    }

    @Override
    public List<OrderDto> getOrderHistory(Long clientId) {
           List<Order> orders = orderRepository.findByClientId(clientId);
        System.out.println("\n\n\n\n\n\norders = " + orders);
        System.out.println("clientId = " + clientId);
        System.out.println(
                "orders.stream().map(this::convertToOrderDto).collect(Collectors.toList()) = " + orders.stream().map(this::convertToOrderDto).collect(Collectors.toList())
        );
        System.out.println("entrou aqui\n\n\n\n\n\n");
            return orders.stream()
                    .map(this::convertToOrderDto)
                    .collect(Collectors.toList());
    }

    private OrderDto convertToOrderDto(Order order) {
        // Implemente a conversão aqui
        return new OrderDto(
                order.getOrigin(),
                order.getDestination(),
                order.getValue(),
                order.getDescription(),
                order.getFeedback()
        );
    }


}
