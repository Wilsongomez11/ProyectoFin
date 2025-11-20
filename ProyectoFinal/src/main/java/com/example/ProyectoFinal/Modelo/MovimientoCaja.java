package com.example.ProyectoFinal.Modelo;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class MovimientoCaja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo;
    private Double monto;
    private String descripcion;
    private LocalDate fecha;

    @ManyToOne
    private Administrador administrador;

    @ManyToOne
    private Caja caja;

    public MovimientoCaja() {}

    public MovimientoCaja(Long id, String tipo, Double monto, String descripcion, LocalDate fecha, Administrador administrador, Caja caja) {
        this.id = id;
        this.tipo = tipo;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = fecha;
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

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
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

    @Override
    public String toString() {
        return "MovimientoCaja{" +
                "id=" + id +
                ", tipo='" + tipo + '\'' +
                ", monto=" + monto +
                ", descripcion='" + descripcion + '\'' +
                ", fecha=" + fecha +
                ", administrador=" + administrador +
                ", caja=" + caja +
                '}';
    }
}

