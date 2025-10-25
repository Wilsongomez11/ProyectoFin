package com.example.ProyectoFinal.Repository;

import com.example.ProyectoFinal.Modelo.Caja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CajaRepository extends JpaRepository<Caja, Long> {
}