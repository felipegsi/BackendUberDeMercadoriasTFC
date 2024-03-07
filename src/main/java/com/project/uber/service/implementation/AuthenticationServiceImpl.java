package com.project.uber.service.implementation;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.project.uber.model.Client;

import com.project.uber.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.project.uber.dtos.AuthDto;
import com.project.uber.service.interfac.AuthenticationService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private ClientRepository clientRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return clientRepository.findByEmail(email);
    }

    @Override
    public String getTokenJwt(AuthDto authDto) {
        Client client = clientRepository.findByEmail(authDto.email());
        return generateTokenJwt(client);
    }

    public  String generateTokenJwt(Client client) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("my-secret");

            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(client.getEmail())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao tentar gerar o token! " +exception.getMessage());
        }
    }

    public String validateTokenJwt(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("my-secret");

            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException exception) {
            return "";
        }
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now()
                .plusHours(8)
                .toInstant(ZoneOffset.of("-03:00"));
    }
}
