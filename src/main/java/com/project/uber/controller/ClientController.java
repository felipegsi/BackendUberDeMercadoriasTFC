package com.project.uber.controller;

import com.project.uber.dtos.*;
import com.project.uber.infra.exceptions.BusinessException;
import com.project.uber.service.implementation.EmailServiceImpl;
import com.project.uber.service.interfac.AuthenticationService;
import com.project.uber.service.interfac.ClientService;

import com.project.uber.service.interfac.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
    private EmailServiceImpl emailService;
    @Autowired
    private OrderService orderService;

    @PostMapping("/register")
    private ClientDto save(@RequestBody ClientDto clientDto) {
        try {
            // Código para registrar o cliente
            return clientService.saveClient(clientDto);
        } catch (BusinessException e) {
            // Retorna um erro 400 (BAD REQUEST) com a mensagem de erro
            throw new BusinessException("Error registering client: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> auth(@RequestBody AuthDto authDto) {
        if (authDto == null || authDto.email() == null || authDto.password() == null) {
            throw new BusinessException("Email and password are mandatory.");
        }
        try {
            var usuarioAutenticationToken = new UsernamePasswordAuthenticationToken(authDto.email(), authDto.password());
            authenticationManager.authenticate(usuarioAutenticationToken);
            String token = authenticationService.getClientTokenJwt(authDto);
            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            // Lançar BusinessException se a autenticação falhar
            throw new BusinessException("Error authenticating client: " + e.getMessage());
        }
    }

    @GetMapping("/viewProfile")
    public ResponseEntity<?> viewProfile(
            @RequestHeader("Authorization") String token) {
        // Valida o token e obtém o username (subject do token)
        Long clientId = validateTokenAndGetClientId(token);

        ClientDto clientDto = clientService.viewProfile(clientId);
        return new ResponseEntity<>(clientDto, HttpStatus.OK);
    }

    @PostMapping("/editProfile")
    public ResponseEntity<?> editProfile(
            @RequestBody ClientDto clientDto, // aqui eu que vou inserir os dados que quero alterar
            @RequestHeader("Authorization") String token) {
        // Valida o token e obtém o username (subject do token)
        Long clientId = validateTokenAndGetClientId(token);

        ClientDto newClient = clientService.editProfile(clientId, clientDto);
        return new ResponseEntity<>(newClient, HttpStatus.OK);
    }

    @PostMapping("/createOrder")
    public ResponseEntity<?> createOrder(@RequestBody OrderDto orderDto,
                                         @RequestHeader("Authorization") String token) {
        try {
            // Valida o token e obtém o Id (subject do token)
            Long clientId = validateTokenAndGetClientId(token);

            if (orderDto == null) {
                throw new BusinessException("Order not found.");
            }

            // Cria o pedido usando o email obtido do token
            OrderDto order = orderService.saveOrder(orderDto, clientId);

            // Retorna a entidade Order recém-criada ou outro DTO que represente o resultado do pedido
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (BusinessException e) {
            throw new BusinessException("Error creating order " + e.getMessage());
        }
    }

    private Long validateTokenAndGetClientId(String token) {

        // Extrai o token do cabeçalho Authorization (assumindo que ele vem no formato "Bearer token")
        String tokenSliced = token.substring("Bearer ".length());

        Long clientId = authenticationService.getClientIdFromToken(tokenSliced);
        if (clientId == null || clientId <= 0) {
            throw new BusinessException("Client not found.");
        }
        return clientId;
    }
    //funçao apenas para validar se o token é valido e se o cliente existe


    @GetMapping("/orderHistory")
    public ResponseEntity<List<OrderDto>> getOrderHistory(
            @RequestHeader("Authorization") String token) {
        try {
            // Valida o token e obtém o username (subject do token)
            Long clientId = validateTokenAndGetClientId(token);

            List<OrderDto> orderHistory = orderService.getClientOrderHistory(clientId);
            return new ResponseEntity<>(orderHistory, HttpStatus.OK);
        } catch (BusinessException e) {
            throw new BusinessException(e.getMessage());
        }
    }

    //""" deixar como claim principal o id do cliente"""
    // +++colocar as mensagens de erro
    @GetMapping("/deleteClient")
    public ResponseEntity<?> deleteClient(@RequestHeader("Authorization") String token) {
        try {// +++ so posso deletar um cliente se ele deletar todas as ordens

            // Valida o token e obtém o username (subject do token)
            Long clientId = validateTokenAndGetClientId(token);

            clientService.deleteClient(clientId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BusinessException e) {
            throw new BusinessException("Error deleting client " + e.getMessage());
        }

    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordDto changePasswordDto,
            @RequestHeader("Authorization") String token) {
        // Valida o token e obtém o username (subject do token)
        Long clientId = validateTokenAndGetClientId(token);

        clientService.changePassword(clientId, changePasswordDto.oldPassword(), changePasswordDto.newPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }


    // ---------------- Semi-implementado ----------------

    /*
    1. Primeiro, você precisa verificar se o e-mail fornecido existe no banco de dados. Se não existir, você pode retornar um erro.
    2. Se o e-mail existir, você pode gerar um token de redefinição de senha. Este token deve ser único e temporário.
    3. Em seguida, você precisa enviar este token para o e-mail do usuário. Você pode usar um serviço de e-mail para fazer isso.
    4. Quando o usuário clicar no link enviado para o e-mail, ele será redirecionado para uma página onde poderá inserir a nova senha. Esta página deve ser implementada no front-end.
    5. No back-end, você precisa de um método para validar o token e redefinir a senha.
    */

    @PostMapping("/recoverPassword")
    public ResponseEntity<?> recoverPassword(
            @RequestBody String email) {
        // Código para recuperar a senha
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/sendSimpleMessage")
    public ResponseEntity<Void> sendSimpleMessage(@RequestBody EmailDto emailDto) {
        emailService.sendSimpleMessage(emailDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/estimateOrderCost")
    public ResponseEntity<BigDecimal> estimateOrderCost(@RequestBody OrderDto orderDto ,
                                                        @RequestHeader("Authorization") String token) {
        try {
            if (orderDto == null || orderDto.getOrigin() == null || orderDto.getDestination() == null) {
                throw new BusinessException("Origin and destination are mandatory.");
            }

            if(validateTokenAndGetClientId(token) <= 0){
                throw new BusinessException("Client not found.");
            }

            BigDecimal estimatedCost = orderService.estimateOrderCost(orderDto.getOrigin(), orderDto.getDestination());

            return new ResponseEntity<>(estimatedCost, HttpStatus.OK);
        } catch (BusinessException e) {
            throw new BusinessException("Error estimating order cost: " + e.getMessage());
        }
    }

}
