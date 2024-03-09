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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

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
            throw new UsernameNotFoundException("Email não encontrado");
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



}
