package com.project.uber.controller;

import com.project.uber.dtos.AuthDto;
import com.project.uber.dtos.ClientDto;
import com.project.uber.service.interfac.AuthenticationService;
import com.project.uber.service.interfac.ClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ClientService clientService;

    @PostMapping("/register")
    private ClientDto save(@RequestBody ClientDto clientDto) {
        return  clientService.saveClient(clientDto) ;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public String auth(@RequestBody AuthDto authDto) {

        var usuarioAutenticationToken = new UsernamePasswordAuthenticationToken(authDto.email(), authDto.password());

        authenticationManager.authenticate(usuarioAutenticationToken);

        return authenticationService.getTokenJwt(authDto);
    }
    //quero que ao fazer o login, entao apareça os dados do usuario



    @GetMapping("/validate")
    @ResponseStatus(HttpStatus.OK)
    public String validate(@RequestHeader("Authorization") String token) {
        return authenticationService.validateTokenJwt(token);
    }

    //ver o perfil do usuario
    @GetMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    public String profile(@RequestHeader("Authorization") String token) {
        return authenticationService.validateTokenJwt(token);
    }


    //atualizar o perfil do usuario
    @PutMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    public String updateProfile(@RequestHeader("Authorization") String token) {
        return authenticationService.validateTokenJwt(token);
    }

}
