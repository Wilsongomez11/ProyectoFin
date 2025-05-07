package com.example.ProyectoFinal.Modelo;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Pizzero {
    @Id
    @GeneratedValue
    private Long id;
    private String nombre;

    @ManyToOne
    private Administrador administrador;

    public Pizzero() {
    }

    public Pizzero(Long id, String nombre, Administrador administrador) {
        this.id = id;
        this.nombre = nombre;
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

    public Administrador getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }

    @Override
    public String toString() {
        return "Pizzero{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", administrador=" + administrador +
                '}';
    }
}

