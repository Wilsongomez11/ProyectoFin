package com.example.ProyectoFinal.Modelo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class MovimientoCaja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo;
    private Double monto;
    private String descripcion;
    private LocalDateTime fecha;


    @ManyToOne
    @JoinColumn(name = "pedido_id")
    @JsonIgnoreProperties({"detalles", "mesa", "cliente", "mesero"})
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "administrador_id")
    @JsonIgnoreProperties({"password"})
    private Administrador administrador;

    @ManyToOne
    private Caja caja;

    public MovimientoCaja() {}

    public MovimientoCaja(Long id, String tipo, Double monto, String descripcion, LocalDateTime fecha, Pedido pedido, Administrador administrador, Caja caja) {
        this.id = id;
        this.tipo = tipo;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.pedido = pedido;
        this.administrador = administrador;
        this.caja = caja;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }

    public Caja getCaja() {
        return caja;
    }

    public void setCaja(Caja caja) {
        this.caja = caja;
    }
}

