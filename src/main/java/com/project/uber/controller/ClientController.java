package com.project.uber.controller;

import com.project.uber.dtos.ClientDto;
import com.project.uber.service.interfac.ClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/register")
    private ClientDto save(@RequestBody ClientDto clientDto) {
        return  clientService.saveClient(clientDto) ;
    }

}
