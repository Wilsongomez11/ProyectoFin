package com.example.ProyectoFinal.Controller;

import com.example.ProyectoFinal.Modelo.Mesero;
import com.example.ProyectoFinal.Service.MeseroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meseros")
public class MeseroController {

    @Autowired
    private MeseroService meseroService;

    @GetMapping
    public List<Mesero> getAll() {
        return meseroService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mesero> getById(@PathVariable Long id) {
        return meseroService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mesero create(@RequestBody Mesero mesero) {
        return meseroService.save(mesero);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mesero> update(@PathVariable Long id, @RequestBody Mesero mesero) {
        return meseroService.findById(id)
                .map(existing -> {
                    existing.setNombre(mesero.getNombre());
                    return ResponseEntity.ok(meseroService.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (meseroService.findById(id).isPresent()) {
            meseroService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

