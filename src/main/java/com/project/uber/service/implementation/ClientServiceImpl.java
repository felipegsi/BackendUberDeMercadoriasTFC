package com.project.uber.service.implementation;

import com.project.uber.dtos.ClientDto;
import com.project.uber.infra.exceptions.BusinessException;
import com.project.uber.model.Client;
import com.project.uber.repository.ClientRepository;
import com.project.uber.service.interfac.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ClientDto saveClient(ClientDto clientDto) {

        Client clientAlredyExists = clientRepository.findByEmail(clientDto.email());

        if (clientAlredyExists != null) {
            throw new BusinessException("Client already exists!");
        }

        var passwordHash = passwordEncoder.encode(clientDto.password());

        Client newClient = clientRepository.save(new Client(clientDto.name(), clientDto.email(), passwordHash,
                clientDto.phoneNumber(), clientDto.taxPayerNumber(), clientDto.street(),
                clientDto.city(), clientDto.postalCode()));

        return new ClientDto(newClient.getName(), newClient.getEmail(), newClient.getPassword(),
                newClient.getPhoneNumber(), newClient.getTaxPayerNumber(), newClient.getStreet(),
                newClient.getCity(), newClient.getPostalCode());
    }

    @Override
    public void deleteClient(Long clientId) {
        clientRepository.deleteById(clientId);
    }

    @Override
    public Client getClientById(Long clientId) {
        return clientRepository.findById(clientId).orElseThrow(() -> new BusinessException("Client not found"));
    }

    @Override
    public Client getClientByEmail(String email) {
        Client client = clientRepository.findByEmail(email);
        if (client == null) {
            throw new BusinessException("Client not found");
        }
        return client;
    }

    @Override
    public void changePassword(Long clientId, String oldPassword, String newPassword) {

        Client client = getClientById(clientId);
        if (!passwordEncoder.matches(oldPassword, client.getPassword())) {
            throw new BusinessException("Invalid password");
        }

        client.setPassword(passwordEncoder.encode(newPassword));
        clientRepository.save(client);
    }

    @Override
    public ClientDto viewProfile(Long clientId) {
        Client client = getClientById(clientId);
        return new ClientDto(client.getName(), client.getEmail(), client.getPassword(),
                client.getPhoneNumber(), client.getTaxPayerNumber(), client.getStreet(),
                client.getCity(), client.getPostalCode());
    }

    @Override
    public ClientDto editProfile(Long clientId, ClientDto clientDto) {
        Client client = getClientById(clientId);
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
