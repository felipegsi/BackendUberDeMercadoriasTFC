package com.project.uberdemercadoriastfc.controller;

import com.project.uberdemercadoriastfc.service.ClienteService;
import com.project.uberdemercadoriastfc.model.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public List<Cliente> listarClientes() {
        return clienteService.listarClientes();
    }

    @GetMapping("/{id}")
    public Cliente buscarCliente(@PathVariable Long id) {
        return clienteService.buscarCliente(id);
    }

    @PostMapping
    public Cliente adicionarCliente(@RequestBody Cliente cliente) {
        System.out.println("\n\nAdicionando cliente: " + cliente.getNome() + " - " + cliente.getEmail() + "\n\n");
        return clienteService.salvarCliente(cliente);
    }

    @PutMapping("/{id}")
    public Cliente atualizarCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        Cliente clienteExistente = clienteService.buscarCliente(id);
        if (clienteExistente != null) {
            cliente.setId(id);
            return clienteService.salvarCliente(cliente);
        } else {
            return null; // Cliente não encontrado
        }
    }

    @DeleteMapping("/{id}")
    public void deletarCliente(@PathVariable Long id) {
        clienteService.deletarCliente(id);
    }
}
