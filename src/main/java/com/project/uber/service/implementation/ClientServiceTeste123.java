package com.project.uber.service.implementation;

import com.project.uber.model.ClientTest123;
import com.project.uber.repository.ClientRepositoryTest123;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceTeste123 {

    private final ClientRepositoryTest123 clientRepositoryTest123;

    @Autowired
    public ClientServiceTeste123(ClientRepositoryTest123 clientRepositoryTest123) {
        this.clientRepositoryTest123 = clientRepositoryTest123;
    }

    public ClientTest123 registerClient(ClientTest123 clientTest123) {
        // Aqui, você pode adicionar lógica de negócios adicional antes de salvar o cliente
        return clientRepositoryTest123.save(clientTest123);
    }

    // Método para email
}
