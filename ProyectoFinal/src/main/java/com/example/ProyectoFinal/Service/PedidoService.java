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
        return pedidoRepository.findAll(Sort.by(Sort.Direction.ASC, "fecha"));
    }

    public Optional<Pedido> findById(Long id) {
        return pedidoRepository.findById(id);
    }

    public Map<Integer, String> getEstadoMesas() {
        List<Pedido> pedidosPendientes = pedidoRepository.findByEstado("Pendiente");

        Set<Integer> mesasOcupadas = pedidosPendientes.stream()
                .filter(p -> p.getMesa() != null)
                .map(p -> p.getMesa().getNumero())
                .collect(Collectors.toSet());

        Map<Integer, String> estadoMesas = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            estadoMesas.put(i, mesasOcupadas.contains(i) ? "Ocupada" : "Libre");
        }

        return estadoMesas;
    }

    private void verificarStockCompleto(Producto producto, int cantidadRequerida) {
        if (producto.getCantidad() < cantidadRequerida) {
            throw new RuntimeException("Stock insuficiente de producto: " + producto.getNombre()
                    + " (Disponible: " + producto.getCantidad() + ", Requerido: " + cantidadRequerida + ")");
        }

        List<ProductoInsumo> insumosNecesarios = productoInsumoRepository.findByProductoId(producto.getId());

        for (ProductoInsumo productoInsumo : insumosNecesarios) {
            Insumo insumo = productoInsumo.getInsumo();
            double cantidadInsumoNecesaria = productoInsumo.getCantidadUsada() * cantidadRequerida;

            if (insumo.getCantidadActual() < cantidadInsumoNecesaria) {
                throw new RuntimeException("Stock insuficiente de insumo: " + insumo.getNombre()
                        + " para el producto " + producto.getNombre()
                        + " (Disponible: " + insumo.getCantidadActual() + " " + insumo.getUnidadMedida()
                        + ", Requerido: " + cantidadInsumoNecesaria + " " + insumo.getUnidadMedida() + ")");
            }
        }
    }

    private void descontarInsumos(Producto producto, int cantidadVendida) {
        List<ProductoInsumo> insumosNecesarios = productoInsumoRepository.findByProductoId(producto.getId());

        for (ProductoInsumo productoInsumo : insumosNecesarios) {
            Insumo insumo = productoInsumo.getInsumo();
            double cantidadADescontar = productoInsumo.getCantidadUsada() * cantidadVendida;

            insumo.setCantidadActual(insumo.getCantidadActual() - cantidadADescontar);
            insumoRepository.save(insumo);
        }
    }

    private void reponerInsumos(Producto producto, int cantidadDevuelta) {
        List<ProductoInsumo> insumosNecesarios = productoInsumoRepository.findByProductoId(producto.getId());

        for (ProductoInsumo productoInsumo : insumosNecesarios) {
            Insumo insumo = productoInsumo.getInsumo();
            double cantidadAReponer = productoInsumo.getCantidadUsada() * cantidadDevuelta;

            insumo.setCantidadActual(insumo.getCantidadActual() + cantidadAReponer);
            insumoRepository.save(insumo);
        }
    }


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

                verificarStockCompleto(producto, detalle.getCantidad());

                producto.setCantidad(producto.getCantidad() - detalle.getCantidad());
                productoRepository.save(producto);

                descontarInsumos(producto, detalle.getCantidad());

                detalle.setPedido(pedido);
                total += detalle.getCantidad() * detalle.getPrecioUnitario();
            }
        }

        pedido.setTotal(total);

        Pedido saved = pedidoRepository.save(pedido);

        if (pedido.getMesa() != null && pedido.getMesa().getId() != null) {
            Mesa mesa = mesaService.findById(pedido.getMesa().getId());
            mesa.setEstado("Ocupada");
            mesaService.save(mesa);
        }

        if (pedido.getDetalles() != null) {
            detalleRepository.saveAll(pedido.getDetalles());
        }

        return saved;
    }



    public void deleteById(Long id) {
        pedidoRepository.deleteById(id);
    }


    public Pedido actualizarEstado(Long id, String estado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        pedido.setEstado(estado);
        return pedidoRepository.save(pedido);
    }


    @Transactional
    public Pedido actualizarPedido(Long id, Pedido pedidoActualizado) {

        return pedidoRepository.findById(id).map(original -> {

            double nuevoTotal = 0.0;

            if (pedidoActualizado.getDetalles() != null) {

                for (DetallePedido dNuevo : pedidoActualizado.getDetalles()) {

                    Producto producto = productoRepository.findById(dNuevo.getProducto().getId())
                            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                    Optional<DetallePedido> anterior = original.getDetalles().stream()
                            .filter(d -> d.getProducto().getId().equals(producto.getId()))
                            .findFirst();

                    if (anterior.isPresent()) {
                        int diferencia = dNuevo.getCantidad() - anterior.get().getCantidad();

                        if (diferencia > 0) {
                            verificarStockCompleto(producto, diferencia);

                            producto.setCantidad(producto.getCantidad() - diferencia);
                            productoRepository.save(producto);

                            descontarInsumos(producto, diferencia);
                        }
                        else if (diferencia < 0) {
                            int cantidadAReponer = Math.abs(diferencia);

                            producto.setCantidad(producto.getCantidad() + cantidadAReponer);
                            productoRepository.save(producto);

                            reponerInsumos(producto, cantidadAReponer);
                        }

                        if (pedidoActualizado.getMesa() != null && pedidoActualizado.getMesa().getId() != null) {
                            Mesa mesa = mesaService.findById(pedidoActualizado.getMesa().getId());
                            if (mesa == null) throw new RuntimeException("Mesa no encontrada");
                            mesa.setEstado("Ocupada");
                            mesaService.save(mesa);
                            original.setMesa(mesa);
                        }
                    }

                    dNuevo.setPedido(original);
                    nuevoTotal += dNuevo.getCantidad() * dNuevo.getPrecioUnitario();
                }

                detalleRepository.saveAll(pedidoActualizado.getDetalles());
                original.setDetalles(pedidoActualizado.getDetalles());
            }

            original.setTotal(nuevoTotal);
            original.setEstado(pedidoActualizado.getEstado());
            original.setMesa(pedidoActualizado.getMesa());
            original.setCliente(pedidoActualizado.getCliente());
            original.setMesero(pedidoActualizado.getMesero());

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
                    Producto producto = productoRepository.findById(productoId)
                            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                    producto.setCantidad(producto.getCantidad() + cantDevuelta);
                    productoRepository.save(producto);

                    reponerInsumos(producto, cantDevuelta);
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
                Mesa mesa = mesaService.findById(pedido.getMesa().getId());
                if (mesa != null) {
                    mesa.setEstado("Libre");
                    mesaService.save(mesa);
                }
            }

        } else {
            pedido.setEstado("Parcialmente Devuelto");
        }

        if (montoDevuelto > 0) {
            movimientoCajaService.registrarDevolucion(id, montoDevuelto, 1L);
        }

        return pedidoRepository.save(pedido);
    }
}