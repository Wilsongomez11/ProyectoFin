package com.example.ProyectoFinal.Repository;

import com.example.ProyectoFinal.Modelo.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FacturaRepository extends JpaRepository<Factura, Long> {

    Optional<Factura> findByPedidoId(Long pedidoId);
    @Query("SELECT f FROM Factura f " + "WHERE f.pedido.fecha BETWEEN :inicio AND :fin")
    List<Factura> findByPedidoFechaBetween(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin
    );
}
