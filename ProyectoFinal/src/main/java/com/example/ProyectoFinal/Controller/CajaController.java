    package com.example.ProyectoFinal.Controller;

    import com.example.ProyectoFinal.Modelo.Caja;
    import com.example.ProyectoFinal.Service.CajaService;
    import com.example.ProyectoFinal.Service.MovimientoCajaService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;
    import java.util.Map;
    @RestController
    @RequestMapping("/caja")
    public class    CajaController {

        @Autowired
        private CajaService cajaService;

        @Autowired
        private MovimientoCajaService movimientoCajaService;

        @GetMapping("/hoy")
        public Caja getCajaHoy() {
            return cajaService.obtenerCajaDelDia();
        }

        @PostMapping("/devolucion")
        public ResponseEntity<?> registrarDevolucion(
                @RequestParam Long pedidoId,
                @RequestParam Double monto,
                @RequestParam Long adminId
        ) {
            movimientoCajaService.registrarDevolucion(pedidoId, monto, adminId);
            return ResponseEntity.ok(Map.of("status", "ok"));
        }
    }
