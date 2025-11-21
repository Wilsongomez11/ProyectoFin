package com.example.ProyectoFinal.Repository;

import com.example.ProyectoFinal.Modelo.Factura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacturaRepository extends JpaRepository<Factura, Long> {
}
