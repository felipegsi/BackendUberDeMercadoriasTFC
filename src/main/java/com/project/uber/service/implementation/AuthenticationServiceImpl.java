package com.project.uber.service.implementation;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.project.uber.model.Client;

import com.project.uber.model.Driver;
import com.project.uber.repository.ClientRepository;
import com.project.uber.repository.DriverRepository;
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

    @Autowired
    private DriverRepository driverRepository; // Adicione um DriverRepository

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetails userDetails = clientRepository.findByEmail(email);
        if (userDetails == null) {
            userDetails = driverRepository.findByEmail(email); // Tente carregar o Driver se o Client não for encontrado
        }
        return userDetails;
    }

    // --------------------------- CLIENT ---------------------------
    @Override
    public String getClientTokenJwt(AuthDto authDto) {
        Client client = clientRepository.findByEmail(authDto.email());
        return generateClientTokenJwt(client);
    }

    // fgsgasggargsgrgar - email, id , senha, telefone, cpf, rua, cidade, cep
    public String generateClientTokenJwt(Client client) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("my-secret");

            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(client.getEmail()) // CONSIDERAR ALTERAR PARA O ID DO CLIENTE
                    .withClaim("clientId", client.getId()) // Adicione o ID do cliente como uma claim
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error when trying to validate the token! Exception: " + exception.getMessage());
        }
    }

    public String getClientEmailFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("my-secret");

            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Error when trying to validate the token! Exception: " + exception.getMessage());
        }
    }

    public Long getClientIdFromToken(String token) {

            if (token == null || token.isEmpty()) {
                throw new IllegalArgumentException("Token is null or empty");
            }
            try {
            Algorithm algorithm = Algorithm.HMAC256("my-secret");

            DecodedJWT jwt = JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token);

            return jwt.getClaim("clientId").asLong();

        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Error when trying to validate the token! Exception: " + exception.getMessage());
        }
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now()
                .plusHours(8)
                .toInstant(ZoneOffset.of("-03:00"));
    }


    // --------------------------- DRIVER ---------------------------


    public String getDriverTokenJwt(AuthDto authDto) {
        Driver driver = driverRepository.findByEmail(authDto.email());
        return generateDriverTokenJwt(driver);
    }

    public String generateDriverTokenJwt(Driver driver) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("my-secret");

            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(driver.getEmail())
                    .withClaim("driverId", driver.getId())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error when trying to validate the token! Exception: " + exception.getMessage());
        }
    }

    public String getDriverEmailFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("my-secret");

            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Error when trying to validate the token! Exception: " + exception.getMessage());
        }
    }

    public Long getDriverIdFromToken(String token) {
        try {
            if (token == null || token.isEmpty()) {
                throw new IllegalArgumentException("Token is null or empty");
            }

            Algorithm algorithm = Algorithm.HMAC256("my-secret");

            DecodedJWT jwt = JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token);

            return jwt.getClaim("driverId").asLong();

        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Error when trying to validate the token! Exception: " + exception.getMessage());
        }
    }


}