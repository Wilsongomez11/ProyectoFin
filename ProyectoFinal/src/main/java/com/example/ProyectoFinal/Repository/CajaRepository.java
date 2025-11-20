package com.example.ProyectoFinal.Repository;

import com.example.ProyectoFinal.Modelo.Caja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CajaRepository extends JpaRepository<Caja, Long> {

    List<Caja> findByFecha(LocalDate fecha);

    @Query("SELECT c FROM Caja c WHERE c.fecha = :fecha ORDER BY c.id DESC")
    Optional<Caja> findFirstByFechaOrderByIdDesc(LocalDate fecha);
}
