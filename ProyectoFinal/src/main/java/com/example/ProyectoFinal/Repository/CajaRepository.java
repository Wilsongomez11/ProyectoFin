package com.example.ProyectoFinal.Repository;

import com.example.ProyectoFinal.Modelo.Caja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CajaRepository extends JpaRepository<Caja, Long> {

    Optional<Caja> findFirstByFechaOrderByIdDesc(LocalDate fecha);

    @Query("SELECT c FROM Caja c WHERE c.fecha = :fecha ORDER BY c.id DESC")
    Optional<Caja> findCajaByFecha(@Param("fecha") LocalDate fecha);
}
