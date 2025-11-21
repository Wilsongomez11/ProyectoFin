package com.example.ProyectoFinal.Controller;

import com.example.ProyectoFinal.Modelo.Factura;
import com.example.ProyectoFinal.Service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/facturas")
@CrossOrigin(origins = "*")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @PostMapping("/generar")
    public Factura generarFactura(
            @RequestParam Long pedidoId,
            @RequestParam String metodoPago,
            @RequestParam(defaultValue = "0") Double propina
    ) {
        return facturaService.generarFactura(pedidoId, metodoPago, propina);
    }

    @GetMapping("/{id}")
    public Factura obtenerFactura(@PathVariable Long id) {
        return facturaService.getFactura(id);
    }
}
