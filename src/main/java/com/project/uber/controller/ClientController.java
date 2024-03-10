package com.project.uber.controller;

import com.project.uber.dtos.AuthDto;
import com.project.uber.dtos.ClientDto;
import com.project.uber.dtos.OrderDto;
import com.project.uber.infra.exceptions.BusinessException;
import com.project.uber.model.Client;
import com.project.uber.model.Order;
import com.project.uber.repository.ClientRepository;
import com.project.uber.service.interfac.AuthenticationService;
import com.project.uber.service.interfac.ClientService;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        return  clientService.saveClient(clientDto) ;
    }

    @PostMapping("/login")
    public ResponseEntity<?> auth(@RequestBody AuthDto authDto) {
        try {
            var usuarioAutenticationToken = new UsernamePasswordAuthenticationToken(authDto.email(), authDto.password());
            authenticationManager.authenticate(usuarioAutenticationToken);
            String token = authenticationService.getTokenJwt(authDto);
            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            // Lançar BusinessException se a autenticação falhar
            throw new BusinessException("Email ou senha inválidos.");
        }
    }

    @GetMapping("/validate")
    @ResponseStatus(HttpStatus.OK)
    public String validate(@RequestHeader("Authorization") String token) {

        System.out.println("\n\n\n\n\n\n\n\n\n\n TOKEN = " + token);
        System.out.println("ENTREI AQUI 1\n\n\n\n\n\n\n\n");
        return authenticationService.validateTokenJwt(token);
    }

    @PostMapping("/createOrder")
    public ResponseEntity<?> createOrder(@RequestBody OrderDto orderDto,
                                         @RequestHeader("Authorization") String authorizationHeader) {
        // Extrai o token do cabeçalho Authorization (assumindo que ele vem no formato "Bearer token")
        String token = authorizationHeader.substring("Bearer ".length());

        // Valida o token e obtém o username (subject do token)
        String email = authenticationService.validateTokenJwt(token);
        System.out.println("\n\n\n\n\n\n\n\n\n\n email = " + email);
        System.out.println("OrderDto = " + orderDto);
        System.out.println("ENTREI AQUI 1\n\n\n\n\n\n\n\n");

        // Caso o token seja inválido, email será null ou vazio
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        // Cria o pedido usando o email obtido do token
        Order order = clientService.createOrder(orderDto, email);

        // Retorna a entidade Order recém-criada ou outro DTO que represente o resultado do pedido
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

        @GetMapping("/{clientId}/orderHistory")
        public ResponseEntity<List<OrderDto>> getOrderHistory(
                @PathVariable Long clientId,
                @RequestHeader("Authorization") String authorizationHeader){


            // Extrai o token do cabeçalho Authorization (assumindo que ele vem no formato "Bearer token")
            String token = authorizationHeader.substring("Bearer ".length());

            // Valida o token e obtém o username (subject do token)
            String email = authenticationService.validateTokenJwt(token);

            if (email == null || email.trim().isEmpty()) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            Client client = clientRepository.findByEmail(email);
            System.out.println("\n\n\n\n\n\n\n\n\n\n email = " + email);
            System.out.println("client = " + client);
            System.out.println("ENTREI AQUI 1\n\n\n\n\n\n\n\n");
            if (!client.getId().equals(clientId)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            List<OrderDto> transportHistory = clientService.getOrderHistory(clientId);
            return new ResponseEntity<>(transportHistory, HttpStatus.OK);
        }







}
