package com.example.ProyectoFinal.Service;

import com.example.ProyectoFinal.Modelo.Administrador;
import com.example.ProyectoFinal.Modelo.Caja;
import com.example.ProyectoFinal.Modelo.MovimientoCaja;
import com.example.ProyectoFinal.Modelo.Pedido;
import com.example.ProyectoFinal.Repository.AdministradorRepository;
import com.example.ProyectoFinal.Repository.CajaRepository;
import com.example.ProyectoFinal.Repository.MovimientoCajaRepository;
import com.example.ProyectoFinal.Repository.PedidoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MovimientoCajaService {

    @Autowired
    private MovimientoCajaRepository movimientoRepo;

    @Autowired
    private PedidoRepository pedidoRepo;

    @Autowired
    private CajaRepository cajaRepo;

    @Autowired
    private AdministradorRepository adminRepo;

    public List<MovimientoCaja> findAll() {
        return movimientoRepo.findAll();
    }

    public MovimientoCaja save(MovimientoCaja m) {
        return movimientoRepo.save(m);
    }

    public List<MovimientoCaja> findByFecha(LocalDate fecha) {
        return movimientoRepo.findByFecha(fecha);
    }

    public List<MovimientoCaja> findHoy() {
        return movimientoRepo.findByFecha(LocalDate.now());
    }

    @Transactional
    public Map<String, Object> registrarPago(Long pedidoId, Double montoPagado, Long adminId) {

        Pedido pedido = pedidoRepo.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        double total = pedido.getTotal();
        if (montoPagado < total)
            throw new RuntimeException("Monto pagado insuficiente");

        double cambio = montoPagado - total;

        Administrador admin = adminRepo.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));

        Caja caja = cajaRepo.findFirstByFechaOrderByIdDesc(LocalDate.now())
                .orElseGet(() -> {
                    Caja nueva = new Caja();
                    nueva.setFecha(LocalDate.now());
                    nueva.setIngresos(0.0);
                    nueva.setEgresos(0.0);
                    nueva.setBalance(0.0);
                    return cajaRepo.save(nueva);
                });

        MovimientoCaja mov = new MovimientoCaja();
        mov.setTipo("ingreso");
        mov.setMonto(total);
        mov.setDescripcion("Pago pedido " + pedidoId);
        mov.setFecha(LocalDate.now());
        mov.setAdministrador(admin);
        mov.setCaja(caja);
        mov.setMonto(Math.abs(total));

        movimientoRepo.save(mov);

        caja.setIngresos(caja.getIngresos() + total);
        caja.setBalance(caja.getIngresos() - caja.getEgresos());
        cajaRepo.save(caja);

        pedido.setEstado("Pagado");
        pedidoRepo.save(pedido);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("total", total);
        respuesta.put("pagado", montoPagado);
        respuesta.put("cambio", cambio);

        return respuesta;
    }

    @Transactional
    public void registrarDevolucion(Long pedidoId, Double monto, Long adminId) {

        Administrador admin = adminRepo.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin no encontrado"));

        Caja caja = cajaRepo.findFirstByFechaOrderByIdDesc(LocalDate.now())
                .orElseGet(() -> {
                    Caja nueva = new Caja();
                    nueva.setFecha(LocalDate.now());
                    nueva.setIngresos(0.0);
                    nueva.setEgresos(0.0);
                    nueva.setBalance(0.0);
                    return cajaRepo.save(nueva);
                });

        MovimientoCaja mov = new MovimientoCaja();
        mov.setTipo("devolucion");
        mov.setMonto(monto);
        mov.setDescripcion("Devolución del pedido " + pedidoId);
        mov.setFecha(LocalDate.now());
        mov.setAdministrador(admin);
        mov.setCaja(caja);
        mov.setMonto(-Math.abs(monto));

        movimientoRepo.save(mov);

        caja.setEgresos(caja.getEgresos() + monto);
        caja.setBalance(caja.getIngresos() - caja.getEgresos());
        cajaRepo.save(caja);
    }
}