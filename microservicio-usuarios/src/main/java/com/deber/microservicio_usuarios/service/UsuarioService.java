package com.deber.microservicio_usuarios.service;

import org.springframework.stereotype.Service;

import com.deber.microservicio_usuarios.model.Usuario;

import java.util.Arrays;
import java.util.List;

@Service
public class UsuarioService {

    private List<Usuario> usuarios = Arrays.asList(
        new Usuario(1L, "Carlos Pérez", "carlos@email.com"),
        new Usuario(2L, "María López", "maria@email.com"),
        new Usuario(3L, "Juan Torres", "juan@email.com")
    );

    public List<Usuario> obtenerTodos() {
        return usuarios;
    }

    public Usuario obtenerPorId(Long id) {
        return usuarios.stream()
            .filter(u -> u.getId().equals(id))
            .findFirst()
            .orElse(null);
    }
}