package com.example.ProyectoFinal.Repository;

import com.example.ProyectoFinal.Modelo.Pizzero;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PizzeroRepository extends JpaRepository<Pizzero, Long> {
    Optional<Pizzero> findByUsernameAndPassword(String username, String password);
}
