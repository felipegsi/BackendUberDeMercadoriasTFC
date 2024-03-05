package com.project.uber.dtos;


import com.project.uber.enums.RoleEnum;

public record UsuarioDto(
        String nome,
        String login,
        String senha,
        RoleEnum role
) {
}
