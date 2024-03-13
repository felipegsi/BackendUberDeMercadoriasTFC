package com.project.uber.controller;

import com.project.uber.dtos.AuthDto;
import com.project.uber.dtos.DriverDto;
import com.project.uber.dtos.OrderDto;
import com.project.uber.infra.exceptions.BusinessException;
import com.project.uber.model.Driver;
import com.project.uber.repository.DriverRepository;
import com.project.uber.service.interfac.AuthenticationService;
import com.project.uber.service.interfac.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//notas: logout no front-end, e no back-end, o token é invalidado.
//implementar a mudança de senha
//implementar a recuperação de senha
@RestController
@RequestMapping("/driver")
public class DriverController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private DriverService driverService;

    @Autowired
    private DriverRepository driverRepository;
    @PostMapping("/register")
    private DriverDto save(@RequestBody DriverDto driverDto) {
        return  driverService.saveDriver(driverDto) ;
    }
/*
    @PostMapping("/login")
    public ResponseEntity<?> auth(@RequestBody AuthDto authDto) {
        try {
            var userAutenticationToken = new UsernamePasswordAuthenticationToken(authDto.email(), authDto.password());
            authenticationManager.authenticate(userAutenticationToken);
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
    @PutMapping("/{driverId}/orders/{orderId}/accept")
    public ResponseEntity<?> acceptOrder(@PathVariable Long driverId, @PathVariable Long orderId,
                                         @RequestHeader("Authorization") String authorizationHeader) {
        // Extrai o token do cabeçalho Authorization (assumindo que ele vem no formato "Bearer token")
        String token = authorizationHeader.substring("Bearer ".length());

        // Valida o token e obtém o email (subject do token)
        String email = authenticationService.validateTokenJwt(token);

        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        try {
            // Chama o serviço para aceitar o pedido, passando o ID do pedido e o ID do motorista
            // A implementação do serviço precisa garantir que o motorista que aceita o pedido é o motorista autenticado
            driverService.acceptOrder(orderId, driverId, email); // Adapte o método acceptOrder conforme necessário
            // Retorna uma resposta de sucesso
            return ResponseEntity.ok().body("Order accepted successfully");
        } catch (BusinessException e) {
            // Trata exceções específicas do negócio aqui
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Trata outras exceções genéricas aqui
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while accepting the order.");
        }
    }
    @GetMapping("/{driverId}/orderHistory")
    public ResponseEntity<List<OrderDto>> getOrderHistory(
            @PathVariable Long driverId,
            @RequestHeader("Authorization") String authorizationHeader){


        // Extrai o token do cabeçalho Authorization (assumindo que ele vem no formato "Bearer token")
        String token = authorizationHeader.substring("Bearer ".length());

        // Valida o token e obtém o username (subject do token)
        String email = authenticationService.validateTokenJwt(token);

        if (email == null || email.trim().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<Driver> driver = driverRepository.findByEmail(email);
        System.out.println("\n\n\n\n\n\n\n\n\n\n email = " + email);
        System.out.println("client = " + driver);
        System.out.println("ENTREI AQUI 1\n\n\n\n\n\n\n\n");
        if (!driver.get().getId().equals(driverId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<OrderDto> transportHistory = driverService.getOrderHistory(driverId);
        return new ResponseEntity<>(transportHistory, HttpStatus.OK);
    }*/

}
