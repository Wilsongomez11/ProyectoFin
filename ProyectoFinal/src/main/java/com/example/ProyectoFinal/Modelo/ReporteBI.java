package com.example.ProyectoFinal.Modelo;

import java.time.LocalDate;

public class ReporteBI {
    private String nombreReporte;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Double totalVentas;
    private Double totalEgresos;
    private Double utilidad;

    public ReporteBI() {
    }

    public ReporteBI(String nombreReporte, LocalDate fechaInicio, LocalDate fechaFin, Double totalVentas, Double totalEgresos, Double utilidad) {
        this.nombreReporte = nombreReporte;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.totalVentas = totalVentas;
        this.totalEgresos = totalEgresos;
        this.utilidad = utilidad;
    }

    public String getNombreReporte() {
        return nombreReporte;
    }

    public void setNombreReporte(String nombreReporte) {
        this.nombreReporte = nombreReporte;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Double getTotalVentas() {
        return totalVentas;
    }

    public void setTotalVentas(Double totalVentas) {
        this.totalVentas = totalVentas;
    }

    public Double getTotalEgresos() {
        return totalEgresos;
    }

    public void setTotalEgresos(Double totalEgresos) {
        this.totalEgresos = totalEgresos;
    }

    public Double getUtilidad() {
        return utilidad;
    }

    public void setUtilidad(Double utilidad) {
        this.utilidad = utilidad;
    }

    @Override
    public String toString() {
        return "ReporteBI{" +
                "nombreReporte='" + nombreReporte + '\'' +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", totalVentas=" + totalVentas +
                ", totalEgresos=" + totalEgresos +
                ", utilidad=" + utilidad +
                '}';
    }
}
