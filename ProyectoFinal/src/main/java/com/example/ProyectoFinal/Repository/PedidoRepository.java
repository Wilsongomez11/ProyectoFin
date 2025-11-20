package com.example.ProyectoFinal.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.example.ProyectoFinal.Modelo.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByEstado(String estado);

    @Query("SELECT p FROM Pedido p WHERE p.estado = 'Pendiente' AND p.mesa IS NOT NULL")
    List<Pedido> findPedidosPendientesConMesa();
}