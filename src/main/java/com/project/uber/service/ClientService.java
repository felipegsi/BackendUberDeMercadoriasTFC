package com.project.uber.service;

import com.project.uber.model.Client;
import com.project.uber.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client registerClient(Client client) {
        // Aqui, você pode adicionar lógica de negócios adicional antes de salvar o cliente
        return clientRepository.save(client);
    }
}
