package com.example.ProyectoFinal.Repository;

import com.example.ProyectoFinal.Modelo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}