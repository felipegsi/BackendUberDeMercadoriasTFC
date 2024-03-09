package com.project.uber.controller;

import com.project.uber.dtos.AuthDto;
import com.project.uber.dtos.ClientDto;
import com.project.uber.dtos.OrderDto;
import com.project.uber.model.Client;
import com.project.uber.model.Order;
import com.project.uber.service.interfac.AuthenticationService;
import com.project.uber.service.interfac.ClientService;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

//notas: logout no front-end, e no back-end, o token é invalidado.
//implementar a mudança de senha
//implementar a recuperação de senha
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

    @PostMapping("/createOrder")
    public ResponseEntity<?> createOrder(@RequestBody OrderDto orderDto,
                                         @RequestHeader("Authorization") String authorizationHeader) {
        // Extrai o token do cabeçalho Authorization (assumindo que ele vem no formato "Bearer token")
        String token = authorizationHeader.substring("Bearer ".length());

        // Valida o token e obtém o username (subject do token)
        String username = authenticationService.validateTokenJwt(token);

        // Caso o token seja inválido, username será null ou vazio
        if (username == null || username.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        // Cria o pedido usando o username obtido do token
        Order order = clientService.createOrder(orderDto, username);

        // Retorna a entidade Order recém-criada ou outro DTO que represente o resultado do pedido
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }



}
