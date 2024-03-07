package com.project.uber.controller;

import com.project.uber.model.ClientTest123;
import com.project.uber.service.implementation.ClientServiceTeste123;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class ClientControllerTest123 {

    private final ClientServiceTeste123 clientServiceTeste123;

    @Autowired
    public ClientControllerTest123(ClientServiceTeste123 clientServiceTeste123) {
        this.clientServiceTeste123 = clientServiceTeste123;
    }

    @PostMapping("/register123")
    public ResponseEntity<ClientTest123> registerClient(@RequestBody ClientTest123 clientTest123) {
        ClientTest123 registeredClientTest123 = clientServiceTeste123.registerClient(clientTest123);
        return ResponseEntity.ok(registeredClientTest123);
    }

}
