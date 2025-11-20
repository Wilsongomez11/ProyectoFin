package com.example.ProyectoFinal.Controller;

import com.example.ProyectoFinal.Modelo.Mesa;
import com.example.ProyectoFinal.Service.MesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/mesas")
public class MesaController {

    @Autowired
    private MesaService mesaService;

    @GetMapping
    public ResponseEntity<List<Mesa>> getAll() {
        List<Mesa> mesas = mesaService.findAll();
        if (mesas.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(mesas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mesa> getById(@PathVariable Long id) {
        Mesa mesa = mesaService.findById(id);
        if (mesa == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(mesa);
    }

    @PostMapping
    public ResponseEntity<Mesa> crearMesa(@RequestBody Mesa mesa) {
        return ResponseEntity.ok(mesaService.save(mesa));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mesa> actualizarMesa(
            @PathVariable Long id,
            @RequestBody Mesa mesaNueva
    ) {
        Mesa actualizada = mesaService.update(id, mesaNueva);
        return ResponseEntity.ok(actualizada);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Mesa> cambiarEstado(
            @PathVariable Long id,
            @RequestParam String estado
    ) {
        Mesa mesa = mesaService.actualizarEstado(id, estado);
        return ResponseEntity.ok(mesa);
    }
}

