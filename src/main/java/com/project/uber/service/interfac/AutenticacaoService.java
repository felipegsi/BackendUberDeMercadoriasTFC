package com.project.uber.service.interfac;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.project.uber.dtos.AuthDto;

public interface AutenticacaoService extends UserDetailsService {
    public String obterToken(AuthDto authDto);
    public String validaTokenJwt(String token);
}
