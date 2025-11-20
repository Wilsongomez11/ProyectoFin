package com.example.ProyectoFinal.Controller;

import com.example.ProyectoFinal.Service.MovimientoCajaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pagos")
@CrossOrigin(origins = "*")
public class PagoController {

    @Autowired
    private MovimientoCajaService movimientoService;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarPago(
            @RequestParam Long pedidoId,
            @RequestParam Double montoPagado,
            @RequestParam Long adminId
    ) {
        return ResponseEntity.ok(
                movimientoService.registrarPago(pedidoId, montoPagado, adminId)
        );
    }
}

