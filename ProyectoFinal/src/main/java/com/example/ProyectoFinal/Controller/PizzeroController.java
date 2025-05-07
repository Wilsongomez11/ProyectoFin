package com.example.ProyectoFinal.Controller;

import com.example.ProyectoFinal.Modelo.Pizzero;
import com.example.ProyectoFinal.Service.PizzeroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pizeros")
public class PizzeroController {

    @Autowired
    private PizzeroService pizzeroService;
    @GetMapping
    public List<Pizzero> getAll() {
        return pizzeroService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pizzero> getById(@PathVariable Long id) {
        return pizzeroService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Pizzero create(@RequestBody Pizzero pizzero) {
        return pizzeroService.save(pizzero);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pizzero> update(@PathVariable Long id, @RequestBody Pizzero pizzero) {
        return pizzeroService.findById(id)
                .map(existing -> {
                    existing.setNombre(pizzero.getNombre());
                    return ResponseEntity.ok(pizzeroService.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (pizzeroService.findById(id).isPresent()) {
            pizzeroService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

