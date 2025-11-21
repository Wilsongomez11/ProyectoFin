package com.example.ProyectoFinal.Modelo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numero;
    private LocalDateTime fechaEmision;
    private String clienteNombre;
    private String clienteDocumento;
    private String metodoPago;
    private Double subtotal;
    private Double propina;
    private Double impuestos;
    private Double total;
    private String estado;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    public Factura() {
    }

    public Factura(Long id, String numero, LocalDateTime fechaEmision, String clienteNombre, String clienteDocumento, String metodoPago, Double subtotal, Double propina, Double impuestos, Double total, String estado, Pedido pedido) {
        this.id = id;
        this.numero = numero;
        this.fechaEmision = fechaEmision;
        this.clienteNombre = clienteNombre;
        this.clienteDocumento = clienteDocumento;
        this.metodoPago = metodoPago;
        this.subtotal = subtotal;
        this.propina = propina;
        this.impuestos = impuestos;
        this.total = total;
        this.estado = estado;
        this.pedido = pedido;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public String getClienteDocumento() {
        return clienteDocumento;
    }

    public void setClienteDocumento(String clienteDocumento) {
        this.clienteDocumento = clienteDocumento;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getPropina() {
        return propina;
    }

    public void setPropina(Double propina) {
        this.propina = propina;
    }

    public Double getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(Double impuestos) {
        this.impuestos = impuestos;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
}
