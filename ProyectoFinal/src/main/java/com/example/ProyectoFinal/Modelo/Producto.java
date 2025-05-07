package com.example.ProyectoFinal.Modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;


@Entity
public class Producto {
    @Id @GeneratedValue
    private Long id;
    private String nombre;
    private int cantidad;

    @ManyToOne
    private Proveedor proveedor;

    @ManyToOne
    private Administrador administrador;

    public Producto() {
    }

    public Producto(Long id, String nombre, int cantidad, Proveedor proveedor, Administrador administrador) {
        this.id = id;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.proveedor = proveedor;
        this.administrador = administrador;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", cantidad=" + cantidad +
                ", proveedor=" + proveedor +
                ", administrador=" + administrador +
                '}';
    }
}
