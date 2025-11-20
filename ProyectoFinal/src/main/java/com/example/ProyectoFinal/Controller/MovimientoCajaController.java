package com.example.ProyectoFinal.Controller;

import com.example.ProyectoFinal.Modelo.MovimientoCaja;
import com.example.ProyectoFinal.Service.MovimientoCajaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/movimientos")
public class MovimientoCajaController {

    @Autowired
    private MovimientoCajaService movimientoService;

    // GET /movimientos
    @GetMapping
    public List<MovimientoCaja> getAll() {
        return movimientoService.findAll();
    }

    // GET /movimientos/hoy
    @GetMapping("/hoy")
    public List<MovimientoCaja> getHoy() {
        return movimientoService.findHoy();
    }

    // GET /movimientos/{fecha}
    @GetMapping("/{fecha}")
    public List<MovimientoCaja> getByFecha(@PathVariable String fecha) {
        return movimientoService.findByFecha(LocalDate.parse(fecha));
    }

    // POST /movimientos  (crear manual)
    @PostMapping
    public MovimientoCaja create(@RequestBody MovimientoCaja movimiento) {
        return movimientoService.save(movimiento);
    }

    // POST /movimientos/registrar-pago (desde pedidos)
    @PostMapping("/registrar-pago")
    public Map<String, Object> registrarPago(
            @RequestParam Long pedidoId,
            @RequestParam Double montoPagado,
            @RequestParam Long adminId
    ) {
        return movimientoService.registrarPago(pedidoId, montoPagado, adminId);
    }
}
