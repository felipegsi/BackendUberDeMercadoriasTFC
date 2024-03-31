package com.project.uber.controller;

import com.project.uber.dtos.AuthDto;
import com.project.uber.dtos.ClientDto;
import com.project.uber.dtos.DriverDto;
import com.project.uber.infra.exceptions.BusinessException;
import com.project.uber.service.interfac.AuthenticationService;
import com.project.uber.service.interfac.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/register")
    private DriverDto save(@RequestBody DriverDto driverDto) {
        try {
            // Código para registrar o DriverDto
            return driverService.saveDriver(driverDto);
        } catch (BusinessException e) {
            // Retorna um erro 400 (BAD REQUEST) com a mensagem de erro
            throw new BusinessException("Error registering driver: " + e.getMessage());
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
            String token = authenticationService.getDriverTokenJwt(authDto);
            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            // Lançar BusinessException se a autenticação falhar
            throw new BusinessException("Error authenticating driver: " + e.getMessage());
        }
    }
    @GetMapping("/deleteDriver")
    public ResponseEntity<?> deleteDriver(@RequestHeader("Authorization") String token) {
        try {// +++ so posso deletar um driver se ele deletar todas as ordens

            // Valida o token e obtém o username (subject do token)
            Long driverId = validateTokenAndGetDriverId(token);

            driverService.deleteDriver(driverId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BusinessException e) {
            throw new BusinessException("Error deleting client " + e.getMessage());
        }

    }
    @PostMapping("/acceptOrder/{orderId}")
    public ResponseEntity<?> acceptOrder(@RequestHeader("Authorization") String token, @PathVariable Long orderId, @RequestParam String driverEmail) {
        Long driverId = validateTokenAndGetDriverId(token);
        try {
            driverService.acceptOrder(orderId, driverId, driverEmail);
            return ResponseEntity.ok().build();
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/online")
    public ResponseEntity<?> setDriverOnline(@RequestHeader("Authorization") String token){
        Long driverId = validateTokenAndGetDriverId(token);
        try {
            driverService.setDriverOnlineStatus(driverId, true);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/offline")
    public ResponseEntity<?> setDriverOffline(@RequestHeader("Authorization") String token){
        Long driverId = validateTokenAndGetDriverId(token);
        try {
            driverService.setDriverOnlineStatus(driverId, false);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/viewProfile")
    public ResponseEntity<?> viewProfile(
            @RequestHeader("Authorization") String token) {
        // Valida o token e obtém o username (subject do token)
        Long driverId = validateTokenAndGetDriverId(token);

        DriverDto driverDto = driverService.viewProfile(driverId);
        return new ResponseEntity<>(driverDto, HttpStatus.OK);
    }

    @PostMapping("/editProfile")
    public ResponseEntity<?> editProfile(
            @RequestBody DriverDto driverDto, // aqui eu que vou inserir os dados que quero alterar
            @RequestHeader("Authorization") String token) {
        // Valida o token e obtém o username (subject do token)
        Long driverId = validateTokenAndGetDriverId(token);

        DriverDto newDriver = driverService.editProfile(driverId, driverDto);
        return new ResponseEntity<>(newDriver, HttpStatus.OK);
    }

    private Long validateTokenAndGetDriverId(String token) {
        // Extrai o token do cabeçalho Authorization (assumindo que ele vem no formato "Bearer token")
        String tokenSliced = token.substring("Bearer ".length());

        Long driverId = authenticationService.getDriverIdFromToken(tokenSliced);
        if (driverId == null || driverId <= 0) {
            throw new BusinessException("Driver not found.");
        }
        return driverId;
    }
}
