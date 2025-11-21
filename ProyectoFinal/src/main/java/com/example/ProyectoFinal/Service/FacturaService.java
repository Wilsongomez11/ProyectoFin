package com.example.ProyectoFinal.Service;

import com.example.ProyectoFinal.Modelo.Factura;
import com.example.ProyectoFinal.Modelo.Pedido;
import com.example.ProyectoFinal.Repository.FacturaRepository;
import com.example.ProyectoFinal.Repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FacturaService {

    @Autowired
    private FacturaRepository facturaRepo;

    @Autowired
    private PedidoRepository pedidoRepo;

    public Factura generarFactura(Long pedidoId, String metodoPago, Double propina) {
        Pedido pedido = pedidoRepo.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        Factura factura = new Factura(pedido, metodoPago, propina);

        return facturaRepo.save(factura);
    }

    public Factura getFactura(Long id) {
        return facturaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));
    }
}
