package com.deber.microservicio_pedidos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deber.microservicio_pedidos.Client.UsuarioClient;
import com.deber.microservicio_pedidos.model.Pedido;
import com.deber.microservicio_pedidos.model.Usuario;

@Service
public class PedidoService {

    @Autowired
    private UsuarioClient usuarioClient;

    public Pedido obtenerPedido(Long pedidoId, Long usuarioId) {
        Usuario usuario = usuarioClient.obtenerUsuarioPorId(usuarioId);
        return new Pedido(pedidoId, "Laptop Dell", 1200.00, usuario);
    }
}
