package com.deber.microservicio_pedidos.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.deber.microservicio_pedidos.model.Usuario;

@FeignClient(name = "microservicio-usuarios")
public interface UsuarioClient {

    @GetMapping("/usuarios/{id}")
    Usuario obtenerUsuarioPorId(@PathVariable Long id);
}