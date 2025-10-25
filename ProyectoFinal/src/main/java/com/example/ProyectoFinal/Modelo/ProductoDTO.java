package com.example.ProyectoFinal.Modelo;

public class ProductoDTO {
    private String nombre;
    private double precio;
    private int cantidad;
    private Long idProveedor;
    private Long idAdministrador;

    public ProductoDTO() {
    }

    public ProductoDTO(String nombre, double precio, int cantidad, Long idProveedor, Long idAdministrador) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.idProveedor = idProveedor;
        this.idAdministrador = idAdministrador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Long getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Long idProveedor) {
        this.idProveedor = idProveedor;
    }

    public Long getIdAdministrador() {
        return idAdministrador;
    }

    public void setIdAdministrador(Long idAdministrador) {
        this.idAdministrador = idAdministrador;
    }

    @Override
    public String toString() {
        return "ProductoDTO{" +
                "nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", cantidad=" + cantidad +
                ", idProveedor=" + idProveedor +
                ", idAdministrador=" + idAdministrador +
                '}';
    }
}

