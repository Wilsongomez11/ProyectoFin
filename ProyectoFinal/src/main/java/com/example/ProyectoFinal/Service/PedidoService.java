package com.example.ProyectoFinal.Service;

import com.example.ProyectoFinal.Modelo.*;
import com.example.ProyectoFinal.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    @Autowired
    private MovimientoCajaService movimientoCajaService;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private DetallePedidoRepository detalleRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private MesaService mesaService;

    @Autowired
    private ProductoInsumoRepository productoInsumoRepository;

    @Autowired
    private InsumoRepository insumoRepository;

    public List<Pedido> findAll() {
        return pedidoRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Optional<Pedido> findById(Long id) {
        return pedidoRepository.findById(id);
    }

    public Map<Integer, String> getEstadoMesas() {
        List<Pedido> todosPedidos = pedidoRepository.findAll();

        Map<Integer, String> mesasOcupadas = new HashMap<>();

        for (Pedido p : todosPedidos) {
            if (p.getMesa() != null) {
                int numeroMesa = p.getMesa().getNumero();
                String estado = p.getEstado();

                if ("Pendiente".equals(estado)) {
                    mesasOcupadas.put(numeroMesa, "Ocupada");
                }
            }
        }

        Map<Integer, String> estadoMesas = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            estadoMesas.put(i, mesasOcupadas.getOrDefault(i, "Libre"));
        }

        return estadoMesas;
    }
    @Transactional
    public Pedido crearPedido(Pedido pedido) {
        if (pedido.getFecha() == null) {
            pedido.setFecha(LocalDateTime.now());
        }

        if (pedido.getEstado() == null || pedido.getEstado().isEmpty()) {
            pedido.setEstado("Pendiente");
        }

        Double total = 0.0;
        if (pedido.getDetalles() != null) {
            for (DetallePedido detalle : pedido.getDetalles()) {
                detalle.setPedido(pedido);
                total += detalle.getPrecioUnitario() * detalle.getCantidad();
            }
        }
        pedido.setTotal(total);

        Pedido saved = pedidoRepository.save(pedido);

        if (pedido.getDetalles() != null) {
            detalleRepository.saveAll(pedido.getDetalles());
        }

        if (pedido.getDetalles() != null) {
            for (DetallePedido detalle : pedido.getDetalles()) {
                descontarStockProducto(detalle.getProducto().getId(), detalle.getCantidad());
            }
        }
        if (pedido.getMesa() != null && pedido.getMesa().getId() != null) {
            try {
                Mesa mesa = mesaService.findById(pedido.getMesa().getId());
                if (mesa != null) {
                    mesa.setEstado("Ocupada");
                    mesaService.save(mesa);
                }
            } catch (Exception e) {
                System.err.println("Error actualizando mesa: " + e.getMessage());
            }
        }

        return saved;
    }
    @Transactional
    public Pedido actualizarEstado(Long id, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        String estadoAnterior = pedido.getEstado();

        // SOLO cuando se DEVUELVE -> Reponer stock
        if ("Devuelto".equals(nuevoEstado) && "Pagado".equals(estadoAnterior)) {
            for (DetallePedido detalle : pedido.getDetalles()) {
                reponerStockProducto(detalle.getProducto().getId(), detalle.getCantidad());
            }

            // Liberar mesa
            if (pedido.getMesa() != null) {
                Mesa mesa = mesaService.findById(pedido.getMesa().getId());
                if (mesa != null) {
                    mesa.setEstado("Libre");
                    mesaService.save(mesa);
                }
            }
        }

        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido actualizarPedido(Long id, Pedido pedidoActualizado) {
        return pedidoRepository.findById(id).map(original -> {
            double nuevoTotal = 0.0;

            if (pedidoActualizado.getDetalles() != null) {
                for (DetallePedido dNuevo : pedidoActualizado.getDetalles()) {
                    dNuevo.setPedido(original);
                    nuevoTotal += dNuevo.getCantidad() * dNuevo.getPrecioUnitario();
                }

                detalleRepository.saveAll(pedidoActualizado.getDetalles());
                original.setDetalles(pedidoActualizado.getDetalles());
            }

            original.setTotal(nuevoTotal);

            if (pedidoActualizado.getMesa() != null) {
                original.setMesa(pedidoActualizado.getMesa());
            }
            if (pedidoActualizado.getCliente() != null) {
                original.setCliente(pedidoActualizado.getCliente());
            }
            if (pedidoActualizado.getMesero() != null) {
                original.setMesero(pedidoActualizado.getMesero());
            }

            return pedidoRepository.save(original);
        }).orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    @Transactional
    public Pedido devolverPedidoParcial(Long id, DevolucionRequest req) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        boolean devolvioAlgo = false;
        boolean devolvioTodo = true;
        double montoDevuelto = 0.0;

        for (DetallePedido det : pedido.getDetalles()) {
            Long productoId = det.getProducto().getId();
            int cantDevuelta = req.getCantidades().getOrDefault(productoId, 0);

            if (cantDevuelta > 0) {
                devolvioAlgo = true;

                if (req.isReponerStock()) {
                    reponerStockProducto(productoId, cantDevuelta);
                }

                montoDevuelto += cantDevuelta * det.getPrecioUnitario();

                if (cantDevuelta < det.getCantidad()) {
                    devolvioTodo = false;
                }
            } else {
                devolvioTodo = false;
            }
        }

        if (!devolvioAlgo) {
            pedido.setEstado("Pagado");
        } else if (devolvioTodo) {
            pedido.setEstado("Devuelto");

            if (pedido.getMesa() != null) {
                try {
                    Mesa mesa = mesaService.findById(pedido.getMesa().getId());
                    if (mesa != null) {
                        mesa.setEstado("Libre");
                        mesaService.save(mesa);
                    }
                } catch (Exception e) {
                    System.err.println("Error liberando mesa: " + e.getMessage());
                }
            }
        } else {
            pedido.setEstado("Parcialmente Devuelto");
        }

        return pedidoRepository.save(pedido);
    }

    @Transactional
    public void deleteById(Long id) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);

        if (pedidoOpt.isPresent()) {
            Pedido pedido = pedidoOpt.get();

            if (pedido.getMesa() != null && !"Pagado".equals(pedido.getEstado())) {
                try {
                    Mesa mesa = mesaService.findById(pedido.getMesa().getId());
                    if (mesa != null) {
                        mesa.setEstado("Libre");
                        mesaService.save(mesa);
                    }
                } catch (Exception e) {
                    System.err.println("Error liberando mesa: " + e.getMessage());
                }
            }

            pedidoRepository.deleteById(id);
        }
    }


    private void descontarStockProducto(Long productoId, int cantidad) {
        List<ProductoInsumo> receta = productoInsumoRepository.findByProductoId(productoId);

        for (ProductoInsumo relacion : receta) {
            Insumo insumo = relacion.getInsumo();
            double cantidadADescontar = relacion.getCantidadUsada() * cantidad;

            if (insumo.getCantidadActual() < cantidadADescontar) {
                throw new RuntimeException("Stock insuficiente de " + insumo.getNombre());
            }

            insumo.setCantidadActual(insumo.getCantidadActual() - cantidadADescontar);
            insumoRepository.save(insumo);
        }
    }

    private void reponerStockProducto(Long productoId, int cantidad) {
        List<ProductoInsumo> receta = productoInsumoRepository.findByProductoId(productoId);

        for (ProductoInsumo relacion : receta) {
            Insumo insumo = relacion.getInsumo();
            double cantidadAReponer = relacion.getCantidadUsada() * cantidad;

            insumo.setCantidadActual(insumo.getCantidadActual() + cantidadAReponer);
            insumoRepository.save(insumo);
        }
    }
}