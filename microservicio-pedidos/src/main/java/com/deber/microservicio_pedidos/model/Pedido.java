package com.deber.microservicio_pedidos.model;

public class Pedido {

    private Long id;
    private String producto;
    private Double precio;
    private Usuario usuario;

    public Pedido() {}

    public Pedido(Long id, String producto, Double precio, Usuario usuario) {
        this.id = id;
        this.producto = producto;
        this.precio = precio;
        this.usuario = usuario;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProducto() { return producto; }
    public void setProducto(String producto) { this.producto = producto; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}