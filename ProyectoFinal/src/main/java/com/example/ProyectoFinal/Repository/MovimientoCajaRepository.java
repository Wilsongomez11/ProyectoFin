package com.example.ProyectoFinal.Repository;

import com.example.ProyectoFinal.Modelo.MovimientoCaja;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface MovimientoCajaRepository extends JpaRepository<MovimientoCaja, Long> {
    List<MovimientoCaja> findByFecha(LocalDate fecha);
}

