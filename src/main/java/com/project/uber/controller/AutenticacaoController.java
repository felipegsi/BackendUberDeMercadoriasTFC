package com.project.uber.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import com.project.uber.dtos.AuthDto;
import com.project.uber.service.interfac.AuthenticationService;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public String auth(@RequestBody AuthDto authDto) {

        var usuarioAutenticationToken = new UsernamePasswordAuthenticationToken(authDto.email(), authDto.password());

        authenticationManager.authenticate(usuarioAutenticationToken);

        return authenticationService.getTokenJwt(authDto);
    }
}
