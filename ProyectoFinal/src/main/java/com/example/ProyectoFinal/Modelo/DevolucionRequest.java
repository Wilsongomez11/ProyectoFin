package com.example.ProyectoFinal.Modelo;

import java.util.Map;

public class DevolucionRequest {
    public double monto;
    public boolean reponerStock;
    public Map<Long, Integer> cantidades;

public DevolucionRequest() {}

    public DevolucionRequest(double monto, boolean reponerStock, Map<Long, Integer> cantidades) {
        this.monto = monto;
        this.reponerStock = reponerStock;
        this.cantidades = cantidades;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public boolean isReponerStock() {
        return reponerStock;
    }

    public void setReponerStock(boolean reponerStock) {
        this.reponerStock = reponerStock;
    }

    public Map<Long, Integer> getCantidades() {
        return cantidades;
    }

    public void setCantidades(Map<Long, Integer> cantidades) {
        this.cantidades = cantidades;
    }

    @Override
    public String toString() {
        return "DevolucionRequest{" +
                "monto=" + monto +
                ", reponerStock=" + reponerStock +
                ", cantidades=" + cantidades +
                '}';
    }
}
