package com.example.ProyectoFinal.Service;

import com.example.ProyectoFinal.Modelo.DetallePedido;
import com.example.ProyectoFinal.Modelo.Pedido;
import com.example.ProyectoFinal.Modelo.Producto;
import com.example.ProyectoFinal.Repository.DetallePedidoRepository;
import com.example.ProyectoFinal.Repository.PedidoRepository;
import com.example.ProyectoFinal.Repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private DetallePedidoRepository detalleRepository;

    @Autowired
    private ProductoRepository productoRepository; //  Nuevo repositorio para manejar stock

    // Obtener pedidos ordenados por fecha (FIFO)
    public List<Pedido> findAll() {
        return pedidoRepository.findAll(Sort.by(Sort.Direction.ASC, "fecha"));
    }

    public Optional<Pedido> findById(Long id) {
        return pedidoRepository.findById(id);
    }

    //  Crear un nuevo pedido con control de stock
    @Transactional
    public Pedido save(Pedido pedido) {
        if (pedido.getFecha() == null) {
            pedido.setFecha(LocalDateTime.now());
        }

        double total = 0.0;

        if (pedido.getDetalles() != null) {
            for (DetallePedido detalle : pedido.getDetalles()) {
                Producto producto = productoRepository.findById(detalle.getProducto().getId())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                // Verificar stock suficiente
                if (producto.getCantidad() < detalle.getCantidad()) {
                    throw new RuntimeException("❌ No hay suficiente stock de " + producto.getNombre());
                }

                // Descontar stock
                producto.setCantidad(producto.getCantidad() - detalle.getCantidad());
                productoRepository.save(producto);

                detalle.setPedido(pedido);
                total += detalle.getCantidad() * detalle.getPrecioUnitario();
            }
        }

        pedido.setTotal(total);

        Pedido saved = pedidoRepository.save(pedido);

        if (pedido.getDetalles() != null) {
            detalleRepository.saveAll(pedido.getDetalles());
        }

        return saved;
    }

    public void deleteById(Long id) {
        pedidoRepository.deleteById(id);
    }

    // Actualizar estado del pedido
    public Pedido actualizarEstado(Long id, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }

    // Actualizar un pedido existente con control de stock
    @Transactional
    public Pedido actualizarPedido(Long id, Pedido pedidoActualizado) {
        return pedidoRepository.findById(id).map(p -> {

            // Recalcular el total
            double nuevoTotal = 0.0;

            if (pedidoActualizado.getDetalles() != null) {
                for (DetallePedido detalleNuevo : pedidoActualizado.getDetalles()) {
                    Producto producto = productoRepository.findById(detalleNuevo.getProducto().getId())
                            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                    // Buscar detalle anterior del mismo producto (si existía)
                    Optional<DetallePedido> detalleExistenteOpt = p.getDetalles().stream()
                            .filter(d -> d.getProducto().getId().equals(producto.getId()))
                            .findFirst();

                    if (detalleExistenteOpt.isPresent()) {
                        DetallePedido detalleExistente = detalleExistenteOpt.get();
                        int diferencia = detalleNuevo.getCantidad() - detalleExistente.getCantidad();

                        // Si se aumentó la cantidad, validar stock
                        if (diferencia > 0 && producto.getCantidad() < diferencia) {
                            throw new RuntimeException("❌ Stock insuficiente para " + producto.getNombre());
                        }

                        // Actualizar stock según diferencia
                        producto.setCantidad(producto.getCantidad() - diferencia);
                        productoRepository.save(producto);
                    }

                    detalleNuevo.setPedido(p);
                    nuevoTotal += detalleNuevo.getCantidad() * detalleNuevo.getPrecioUnitario();
                }

                // Guardar los nuevos detalles
                detalleRepository.saveAll(pedidoActualizado.getDetalles());
                p.setDetalles(pedidoActualizado.getDetalles());
            }

            // Actualizar campos generales
            p.setTotal(nuevoTotal);
            p.setEstado(pedidoActualizado.getEstado());
            p.setMesa(pedidoActualizado.getMesa());
            p.setCliente(pedidoActualizado.getCliente());
            p.setMesero(pedidoActualizado.getMesero());

            return pedidoRepository.save(p);
        }).orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }
}
