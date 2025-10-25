package com.example.ProyectoFinal.Modelo;

public class LoginResponse {
    private Long id;
    private String nombre;
    private String username;
    private String cargo;

    public LoginResponse(Long id, String nombre, String username, String cargo) {
        this.id = id;
        this.nombre = nombre;
        this.username = username;
        this.cargo = cargo;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getUsername() { return username; }
    public String getCargo() { return cargo; }
}
