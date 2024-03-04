package com.project.uber.controller;

import com.project.uber.model.Client;
import com.project.uber.service.ClientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    // Método de login atualizado para refletir as mudanças
    @PostMapping("/login")
    public ResponseEntity<Client> login(@RequestBody Map<String, String> credentials, HttpSession session) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        try {
            Client client = clientService.loginClient(email, password);
            session.setAttribute("client", client); // Armazena o cliente na sessão
            return ResponseEntity.ok(client);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);// Retorna o status de erro
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

}
