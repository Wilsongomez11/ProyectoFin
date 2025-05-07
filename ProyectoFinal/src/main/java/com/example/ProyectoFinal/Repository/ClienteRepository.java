package com.example.ProyectoFinal.Repository;

import com.example.ProyectoFinal.Modelo.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}

