package com.example.ProyectoFinal.Repository;

import com.example.ProyectoFinal.Modelo.Mesero;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MeseroRepository extends JpaRepository<Mesero, Long> {
    Optional<Mesero> findByUsernameAndPassword(String username, String password);
}
