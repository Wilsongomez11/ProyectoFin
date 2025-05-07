package com.example.ProyectoFinal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Cliente {
    @Id
    @GeneratedValue
    private Long id;
    private String nombre;

    @ManyToOne
    private Mesero mesero;

    public Cliente() {
    }

    public Cliente(Long id, String nombre, Mesero mesero) {
        this.id = id;
        this.nombre = nombre;
        this.mesero = mesero;
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

    public Mesero getMesero() {
        return mesero;
    }

    public void setMesero(Mesero mesero) {
        this.mesero = mesero;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", mesero=" + mesero +
                '}';
    }
}

