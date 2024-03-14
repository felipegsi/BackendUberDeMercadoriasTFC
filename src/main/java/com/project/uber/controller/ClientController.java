package com.project.uber.controller;

import com.project.uber.dtos.AuthDto;
import com.project.uber.dtos.ChangePasswordDto;
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
            throw new BusinessException("Email ou senha inválidos." + e.getMessage());
        }
    }

    @GetMapping("/validate")
    @ResponseStatus(HttpStatus.OK)
    public String validate(@RequestHeader("Authorization") String authorizationHeader) {
        // Extrai o token do cabeçal
        try {
            String token = authorizationHeader.substring("Bearer ".length());
            String email = authenticationService.getClientEmailFromToken(token);
            Long clientId = authenticationService.getClientIdFromToken(token);
            return "Token válido para o cliente " + email + " com id " + clientId;
        } catch (Exception e) {
            throw new BusinessException("Invalid Token.");
        }
    }

    @PostMapping("/createOrder")
    public ResponseEntity<?> createOrder(@RequestBody OrderDto orderDto,
                                         @RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Extrai o token do cabeçalho Authorization (assumindo que ele vem no formato "Bearer token")
            String token = authorizationHeader.substring("Bearer ".length());

            // Valida o token e obtém o username (subject do token)
            Long clientId = validateTokenAndGetClientId(token);

            if (orderDto == null) {
                throw new BusinessException("Order not found.");
            }

            // Cria o pedido usando o email obtido do token
            Order order = clientService.createOrder(orderDto, clientId);

            // Retorna a entidade Order recém-criada ou outro DTO que represente o resultado do pedido
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (BusinessException e) {
            throw new BusinessException("Error creating order " + e.getMessage());
        }
    }

    private Long validateTokenAndGetClientId(String token) {

        Long clientId = authenticationService.getClientIdFromToken(token);
        if (clientId == null || clientId <= 0) {
            throw new BusinessException("Client not found.");
        }
        return clientId;
    }

    @GetMapping("/orderHistory")
    public ResponseEntity<List<OrderDto>> getOrderHistory(
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Extrai o token do cabeçalho Authorization (assumindo que ele vem no formato "Bearer token")
            String token = authorizationHeader.substring("Bearer ".length());

            // Valida o token e obtém o username (subject do token)
            Long clientId = validateTokenAndGetClientId(token);

            List<OrderDto> transportHistory = clientService.getOrderHistory(clientId);
            return new ResponseEntity<>(transportHistory, HttpStatus.OK);
        } catch (BusinessException e) {
            throw new BusinessException("Error getting order history " + e.getMessage());
        }
    }

    //""" deixar como claim principal o id do cliente"""
    // +++colocar as mensagens de erro
    @GetMapping("/deleteClient")
    public ResponseEntity<?> deleteClient(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Extrai o token do cabeçalho
            String token = authorizationHeader.substring("Bearer ".length());

            // Valida o token e obtém o username (subject do token)
            Long clientId = validateTokenAndGetClientId(token);

            clientService.deleteClient(clientId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BusinessException e) {
            throw new BusinessException("Error deleting client " + e.getMessage());
        }

    }
    //se no email ja extraio o respectivo email do cliente, entao nao preciso passar o id do cliente como parametro?
    // é melhor usar uma classe dto para o change password?
    //fazer o mesmo para as funçoes acima
    //dto cliente sem password
    //numero para string no user
    //por umtimo impl o modelmapper

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordDto changePasswordDto,
            @RequestHeader("Authorization") String authorizationHeader) {

        // Extract the token from the header
        String token = authorizationHeader.substring("Bearer ".length());
        // Valida o token e obtém o username (subject do token)
        Long clientId = validateTokenAndGetClientId(token);

        clientService.changePassword(clientId, changePasswordDto.oldPassword(), changePasswordDto.newPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

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


    @GetMapping("/viewProfile")
    public ResponseEntity<?> viewProfile(
            @RequestHeader("Authorization") String authorizationHeader) {
        // Extract the token from the header
        String token = authorizationHeader.substring("Bearer ".length());
        // Valida o token e obtém o username (subject do token)
        Long clientId = validateTokenAndGetClientId(token);

        ClientDto clientDto = clientService.viewProfile(clientId);
        return new ResponseEntity<>(clientDto, HttpStatus.OK);
    }

    @PostMapping("/editProfile")
    public ResponseEntity<?> editProfile(
            @RequestBody ClientDto clientDto, // aqui eu que vou inserir os dados que quero alterar
            @RequestHeader("Authorization") String authorizationHeader) {
        // Extract the token from the header
        String token = authorizationHeader.substring("Bearer ".length());
        // Valida o token e obtém o username (subject do token)
        Long clientId = validateTokenAndGetClientId(token);

        ClientDto newClient = clientService.editProfile(clientId, clientDto);
        return new ResponseEntity<>(newClient, HttpStatus.OK);
    }


}
