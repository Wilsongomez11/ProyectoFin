package com.example.ProyectoFinal.Modelo;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Mesero {
    @Id
    @GeneratedValue
    private Long id;
    private String nombre;

    @ManyToOne
    private Administrador administrador;

    @OneToMany(mappedBy = "mesero")
    private List<Cliente> clientes;

    public Mesero() {
    }

    public Mesero(Long id, String nombre, Administrador administrador, List<Cliente> clientes) {
        this.id = id;
        this.nombre = nombre;
        this.administrador = administrador;
        this.clientes = clientes;
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

    public Administrador getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    @Override
    public String toString() {
        return "Mesero{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", administrador=" + administrador +
                ", clientes=" + clientes +
                '}';
    }
}

