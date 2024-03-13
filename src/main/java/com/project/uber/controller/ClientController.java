package com.project.uber.controller;

import com.project.uber.dtos.AuthDto;
import com.project.uber.dtos.ClientDto;
import com.project.uber.dtos.OrderDto;
import com.project.uber.infra.exceptions.ApiError;
import com.project.uber.infra.exceptions.BusinessException;
import com.project.uber.model.Order;
import com.project.uber.repository.ClientRepository;
import com.project.uber.service.interfac.AuthenticationService;
import com.project.uber.service.interfac.ClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Autowired
    private ClientRepository clientRepository;

    @PostMapping("/register")
    private ClientDto save(@RequestBody ClientDto clientDto) {
        try {
            // Código para registrar o cliente
            return clientService.saveClient(clientDto);
        } catch (BusinessException e) {
            // Retorna um erro 400 (BAD REQUEST) com a mensagem de erro
            throw new BusinessException("Error registering client " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> auth(@RequestBody AuthDto authDto) {
        try {
            var usuarioAutenticationToken = new UsernamePasswordAuthenticationToken(authDto.email(), authDto.password());
            authenticationManager.authenticate(usuarioAutenticationToken);
            String token = authenticationService.getClientTokenJwt(authDto);
            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            // Lançar BusinessException se a autenticação falhar
            throw new BusinessException("Email ou senha inválidos.");
        }
    }

    @GetMapping("/validate")
    @ResponseStatus(HttpStatus.OK)
    public String validate(@RequestHeader("Authorization") String authorizationHeader) {
        // Extrai o token do cabeçal
        return authenticationService.getClientEmailFromToken(authorizationHeader.substring("Bearer ".length()));
    }

    @PostMapping("/createOrder")
    public ResponseEntity<?> createOrder(@RequestBody OrderDto orderDto,
                                         @RequestHeader("Authorization") String authorizationHeader) {
        // Extrai o token do cabeçalho Authorization (assumindo que ele vem no formato "Bearer token")
        String token = authorizationHeader.substring("Bearer ".length());

        // Valida o token e obtém o username (subject do token)
        Long clientId = authenticationService.getClientIdFromToken(token);

        // Caso o token seja inválido, email será null ou vazio
        if (clientId == null || clientId <= 0) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // Cria o pedido usando o email obtido do token
        Order order = clientService.createOrder(orderDto, clientId);

        // Retorna a entidade Order recém-criada ou outro DTO que represente o resultado do pedido
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/orderHistory")
    public ResponseEntity<List<OrderDto>> getOrderHistory(
            @RequestHeader("Authorization") String authorizationHeader) {

        // Extrai o token do cabeçalho Authorization (assumindo que ele vem no formato "Bearer token")
        String token = authorizationHeader.substring("Bearer ".length());

        // Valida o token e obtém o username (subject do token)
        Long clientId = authenticationService.getClientIdFromToken(token);



        List<OrderDto> transportHistory = clientService.getOrderHistory(clientId);
        return new ResponseEntity<>(transportHistory, HttpStatus.OK);
    }


    //""" deixar como claim principal o id do cliente"""
    // """resuzir o uso da claim para o email
// ++++colocar as mensagens de erro
    @GetMapping("/deleteClient")
    public ResponseEntity<?> deleteClient( //*** apenas deletar se nao tiver pedido em pendente ***
                                           @RequestHeader("Authorization") String authorizationHeader) {

        // Extrai o token do cabeçalho
        String token = authorizationHeader.substring("Bearer ".length());
        //+++  se o token for invalido+++

        Long clientId = authenticationService.getClientIdFromToken(token);

        if (clientId == null || clientId <= 0) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        clientService.deleteClient(clientId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
//se no email ja extraio o respectivo email do cliente, entao nao preciso passar o id do cliente como parametro?
// é melhor usar uma classe dto para o change password?
//fazer o mesmo para as funçoes acima
    //dto cliente sem pass
    //numero para string no user


    //por umtimo impl o modelmapper

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(
            @RequestBody String newPassword,
            @RequestHeader("Authorization") String authorizationHeader) {

        // Extract the token from the header
        String token = authorizationHeader.substring("Bearer ".length());
        String email = authenticationService.getClientEmailFromToken(token);

        if (email == null || email.trim().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        clientService.changePassword(email, newPassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
