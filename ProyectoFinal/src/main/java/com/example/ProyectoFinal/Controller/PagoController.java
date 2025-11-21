package com.example.ProyectoFinal.Controller;

import com.example.ProyectoFinal.Service.MovimientoCajaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/pagos")
@CrossOrigin(origins = "*")
public class PagoController {

    @Autowired
    private MovimientoCajaService movimientoCajaService;

    @PostMapping("/registrar")
    public Map<String, Object> registrarPago(
            @RequestParam Long pedidoId,
            @RequestParam Double montoPagado,
            @RequestParam Long adminId
    ) {
        return movimientoCajaService.registrarPago(pedidoId, montoPagado, adminId);
    }
}

