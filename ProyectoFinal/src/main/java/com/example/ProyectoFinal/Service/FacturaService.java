package com.example.ProyectoFinal.Service;

import com.example.ProyectoFinal.Modelo.GenerarFacturaRequest;
import com.example.ProyectoFinal.Modelo.Factura;
import com.example.ProyectoFinal.Modelo.Pedido;
import com.example.ProyectoFinal.Repository.FacturaRepository;
import com.example.ProyectoFinal.Repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    public Factura generarFactura(GenerarFacturaRequest request) {

        Optional<Pedido> pedidoOpt = pedidoRepository.findById(request.getPedidoId());
        if (pedidoOpt.isEmpty()) {
            throw new RuntimeException("Pedido no encontrado con id " + request.getPedidoId());
        }

        Pedido pedido = pedidoOpt.get();

        Factura factura = new Factura();
        factura.setPedido(pedido);
        factura.setFechaEmision(LocalDateTime.now());

        // Numero simple. Si quieres, luego lo cambias por consecutivo en base de datos
        String numero = "F-" + System.currentTimeMillis();
        factura.setNumero(numero);

        factura.setClienteNombre(request.getClienteNombre());
        factura.setClienteDocumento(request.getClienteDocumento());
        factura.setMetodoPago(request.getMetodoPago());

        Double subtotal = pedido.getTotal(); // ajusta si tu campo se llama diferente
        if (subtotal == null) {
            subtotal = 0.0;
        }
        factura.setSubtotal(subtotal);

        Double propina = request.getPropina() != null ? request.getPropina() : 0.0;
        factura.setPropina(propina);

        Double impuestos = 0.0;
        factura.setImpuestos(impuestos);

        Double total = subtotal + propina + impuestos;
        factura.setTotal(total);

        factura.setEstado("EMITIDA");

        return facturaRepository.save(factura);
    }

    public Optional<Factura> obtenerPorId(Long id) {
        return facturaRepository.findById(id);
    }

    public Optional<Factura> obtenerPorPedido(Long pedidoId) {
        return facturaRepository.findByPedidoId(pedidoId);
    }

    public List<Factura> listarTodas() {
        return facturaRepository.findAll();
    }
}

