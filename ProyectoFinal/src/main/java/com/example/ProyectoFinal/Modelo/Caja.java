package com.example.ProyectoFinal.Modelo;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Caja {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;
    private Double ingresos;
    private Double egresos;
    private Double balance;

    private String tipoCierre;

    @ManyToOne
    private Administrador administrador;

    public Caja() {
    }

    public Caja(Long id, LocalDate fecha, Double ingresos, Double egresos, Double balance, String tipoCierre, Administrador administrador) {
        this.id = id;
        this.fecha = fecha;
        this.ingresos = ingresos;
        this.egresos = egresos;
        this.balance = balance;
        this.tipoCierre = tipoCierre;
        this.administrador = administrador;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Double getIngresos() {
        return ingresos;
    }

    public void setIngresos(Double ingresos) {
        this.ingresos = ingresos;
    }

    public Double getEgresos() {
        return egresos;
    }

    public void setEgresos(Double egresos) {
        this.egresos = egresos;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getTipoCierre() {
        return tipoCierre;
    }

    public void setTipoCierre(String tipoCierre) {
        this.tipoCierre = tipoCierre;
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }

    @Override
    public String toString() {
        return "Caja{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", ingresos=" + ingresos +
                ", egresos=" + egresos +
                ", balance=" + balance +
                ", tipoCierre='" + tipoCierre + '\'' +
                ", administrador=" + administrador +
                '}';
    }
}
