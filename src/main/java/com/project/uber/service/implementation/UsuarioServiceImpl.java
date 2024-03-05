package com.project.uber.service.implementation;

import com.project.uber.dtos.UsuarioDto;
import com.project.uber.infra.exceptions.BusinessException;
import com.project.uber.model.Usuario;
import com.project.uber.repository.UsuarioRepository;
import com.project.uber.service.interfac.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UsuarioDto salvar(UsuarioDto usuarioDto) {

        Usuario usuarioJaExiste = usuarioRepository.findByLogin(usuarioDto.login());

        if (usuarioJaExiste != null) {
            throw new BusinessException("Usuário já existe!");
        }

        var passwordHash = passwordEncoder.encode(usuarioDto.senha());

        Usuario entity = new Usuario(usuarioDto.nome(), usuarioDto.login(), passwordHash);

        Usuario novoUsuario = usuarioRepository.save(entity);

        return new UsuarioDto(novoUsuario.getNome(), novoUsuario.getLogin(), novoUsuario.getSenha());
    }
}
