package com.project.uber.service.interfac;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.project.uber.dtos.AuthDto;

public interface AuthenticationService extends UserDetailsService {
    public String getTokenJwt(AuthDto authDto);
    public String validateTokenJwt(String token);
}
