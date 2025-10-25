package com.example.ProyectoFinal.Repository;

import com.example.ProyectoFinal.Modelo.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsumoRepository extends JpaRepository<Insumo, Long> {
}

