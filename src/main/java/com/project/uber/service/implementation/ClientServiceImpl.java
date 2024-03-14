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

   // @Autowired
   // private JavaMailSender mailSender;


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
    public Order createOrder(OrderDto orderDto, Long clientId) {
        // Localize o cliente pelo email


        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new BusinessException("Client not found"));


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
                order.getValue(),
                order.getDescription(),
                order.getFeedback()
        );
    }

    @Override
    public void deleteClient(Long clientId) {

        clientRepository.deleteById(clientId);
    }

    @Override
    public void changePassword(Long clientId, String oldPassword, String newPassword) {

        Client client = clientRepository.findById(clientId).orElseThrow(() -> new BusinessException("Client not found"));
        if (!passwordEncoder.matches(oldPassword, client.getPassword())) {
            throw new BusinessException("Invalid password");
        }

        client.setPassword(passwordEncoder.encode(newPassword));
        clientRepository.save(client);
    }


    @Override
    public ClientDto viewProfile(Long clientId) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new BusinessException("Client not found"));
        return new ClientDto(client.getName(), client.getEmail(), client.getPassword(),
                client.getPhoneNumber(), client.getTaxPayerNumber(), client.getStreet(),
                client.getCity(), client.getPostalCode());
    }

    @Override
    public ClientDto editProfile(Long clientId, ClientDto clientDto) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new BusinessException("Client not found"));
        client.setName(clientDto.name());
        //tratar os erros

        client.setEmail(clientDto.email());
        client.setPhoneNumber(clientDto.phoneNumber());
        client.setTaxPayerNumber(clientDto.taxPayerNumber());
        client.setStreet(clientDto.street());
        client.setCity(clientDto.city());
        client.setPostalCode(clientDto.postalCode());
        clientRepository.save(client);
        return new ClientDto(client.getName(), client.getEmail(), client.getPassword(),
                client.getPhoneNumber(), client.getTaxPayerNumber(), client.getStreet(),
                client.getCity(), client.getPostalCode());
    }




}
