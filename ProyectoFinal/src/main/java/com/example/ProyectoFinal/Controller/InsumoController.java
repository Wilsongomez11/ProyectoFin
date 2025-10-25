package com.example.ProyectoFinal.Controller;

import com.example.ProyectoFinal.Modelo.Insumo;
import com.example.ProyectoFinal.Service.InsumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/insumos")
public class InsumoController {

    @Autowired
    private InsumoService insumoService;

    @GetMapping
    public List<Insumo> getAll() {
        return insumoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Insumo> getById(@PathVariable Long id) {
        Optional<Insumo> insumo = insumoService.findById(id);
        return insumo.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Insumo> create(@RequestBody Insumo insumo) {
        return ResponseEntity.ok(insumoService.save(insumo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Insumo> update(@PathVariable Long id, @RequestBody Insumo updated) {
        Optional<Insumo> insumoOpt = insumoService.findById(id);
        if (insumoOpt.isPresent()) {
            Insumo insumo = insumoOpt.get();
            insumo.setNombre(updated.getNombre());
            insumo.setUnidadMedida(updated.getUnidadMedida());
            insumo.setCantidadActual(updated.getCantidadActual());
            insumo.setCantidadMinima(updated.getCantidadMinima());
            return ResponseEntity.ok(insumoService.save(insumo));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        insumoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/bajo-stock")
    public List<Insumo> getInsumosBajoStock() {
        return insumoService.getInsumosBajoStock();
    }
}
