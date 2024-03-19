package com.project.uber.controller;

import com.project.uber.dtos.AuthDto;
import com.project.uber.dtos.DriverDto;
import com.project.uber.infra.exceptions.BusinessException;
import com.project.uber.service.interfac.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
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

    //@Autowired
  //  private AuthenticationDriverService authenticationDriverService;

    @Autowired
    private DriverService driverService;


    @PostMapping("/register")
    private DriverDto save(@RequestBody DriverDto driverDto) {
        try {
            // Código para registrar o DriverDto
            return  driverService.saveDriver(driverDto) ;
        } catch (BusinessException e) {
            // Retorna um erro 400 (BAD REQUEST) com a mensagem de erro
            throw new BusinessException("Error registering driver: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> auth(@RequestBody AuthDto authDto) {
        if (authDto == null || authDto.email() == null || authDto.password() == null) {
            throw new BusinessException("Email e senha são obrigatórios.");
        }
        try {
            var usuarioAutenticationToken = new UsernamePasswordAuthenticationToken(authDto.email(), authDto.password());
            //authenticationManager.authenticate(usuarioAutenticationToken);
         //   String token = authenticationDriverService.getDriverTokenJwt(authDto);
            return ResponseEntity.ok("token");
        } catch (AuthenticationException e) {
            // Lançar BusinessException se a autenticação falhar
            throw new BusinessException("Email ou senha inválidos.");
        }
    }

}
