package com.deber.microservicio_pedidos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.deber.microservicio_pedidos.model.Pedido;
import com.deber.microservicio_pedidos.service.PedidoService;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping("/{pedidoId}/usuario/{usuarioId}")
    public Pedido obtenerPedido(
        @PathVariable Long pedidoId,
        @PathVariable Long usuarioId) {
        return pedidoService.obtenerPedido(pedidoId, usuarioId);
    }
}