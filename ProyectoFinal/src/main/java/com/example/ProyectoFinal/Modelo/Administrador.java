package com.example.ProyectoFinal.Modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
    public class Administrador {
        @Id @GeneratedValue
        private Long id;
        private String nombre;

        @OneToMany(mappedBy = "administrador")
        private List<Pizzero> pizzeros;

        @OneToMany(mappedBy = "administrador")
        private List<Mesero> meseros;

        @OneToMany(mappedBy = "administrador")
        private List<Producto> productos;

    public Administrador() {
    }

    public Administrador(Long id, String nombre, List<Pizzero> pizzeros, List<Mesero> meseros, List<Producto> productos) {
        this.id = id;
        this.nombre = nombre;
        this.pizzeros = pizzeros;
        this.meseros = meseros;
        this.productos = productos;
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

    public List<Pizzero> getPizzeros() {
        return pizzeros;
    }

    public void setPizzeros(List<Pizzero> pizzeros) {
        this.pizzeros = pizzeros;
    }

    public List<Mesero> getMeseros() {
        return meseros;
    }

    public void setMeseros(List<Mesero> meseros) {
        this.meseros = meseros;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    @Override
    public String toString() {
        return "Administrador{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", pizzeros=" + pizzeros +
                ", meseros=" + meseros +
                ", productos=" + productos +
                '}';
    }
}

