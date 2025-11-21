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
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MovimientoCajaService {

    @Autowired
    private MovimientoCajaRepository movimientoCajaRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private CajaRepository cajaRepo;


    public List<MovimientoCaja> findAll() {
        return movimientoCajaRepository.findAll();
    }

    public MovimientoCaja save(MovimientoCaja m) {
        return movimientoCajaRepository.save(m);
    }

    public List<MovimientoCaja> findByFecha(LocalDate fecha) {
        return movimientoCajaRepository.findByFecha(fecha);
    }

    public List<MovimientoCaja> findHoy() {
        return movimientoCajaRepository.findByFecha(LocalDate.now());
    }

    @Transactional
    public Map<String, Object> registrarPago(Long pedidoId, Double montoPagado, Long adminId) {

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        Administrador administrador = administradorRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));

        Double total = pedido.getTotal();
        Double cambio = montoPagado - total;


        Caja caja = cajaRepo.findFirstByFechaOrderByIdDesc(LocalDate.now())
                .orElseGet(() -> {
                    Caja nueva = new Caja();
                    nueva.setFecha(LocalDate.now());
                    nueva.setIngresos(0.0);
                    nueva.setEgresos(0.0);
                    nueva.setBalance(0.0);
                    return cajaRepo.save(nueva);
                });


        caja.setIngresos(caja.getIngresos() + total);
        caja.setBalance(caja.getIngresos() - caja.getEgresos());
        cajaRepo.save(caja);


        MovimientoCaja movimiento = new MovimientoCaja();
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setTipo("Pago");
        movimiento.setMonto(total);
        movimiento.setDescripcion("Pago - Pedido #" + pedidoId);
        movimiento.setPedido(pedido);
        movimiento.setAdministrador(administrador);

        movimientoCajaRepository.save(movimiento);


        Map<String, Object> resultado = new HashMap<>();
        resultado.put("mensaje", "Pago registrado exitosamente");
        resultado.put("cambio", cambio);
        resultado.put("total", total);
        resultado.put("pagado", montoPagado);
        resultado.put("balance", caja.getBalance());

        return resultado;
    }
    @Transactional
    public void registrarDevolucion(Long pedidoId, Double monto, Long adminId) {

        Administrador admin = administradorRepository.findById(adminId)
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
        mov.setFecha(LocalDateTime.now());
        mov.setAdministrador(admin);
        mov.setCaja(caja);
        mov.setMonto(-Math.abs(monto));

        movimientoCajaRepository.save(mov);

        caja.setEgresos(caja.getEgresos() + monto);
        caja.setBalance(caja.getIngresos() - caja.getEgresos());
        cajaRepo.save(caja);
    }
}