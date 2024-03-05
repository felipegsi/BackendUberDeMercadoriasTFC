package com.project.uber.controller;

import com.project.uber.model.Client;
import com.project.uber.service.implementation.ClientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/register")
    public ResponseEntity<Client> registerClient(@RequestBody Client client) {
        Client registeredClient = clientService.registerClient(client);
        return ResponseEntity.ok(registeredClient);
    }

}
