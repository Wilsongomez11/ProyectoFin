package com.example.ProyectoFinal.Controller;

import com.example.ProyectoFinal.Modelo.Administrador;
import com.example.ProyectoFinal.Service.AdministradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administradores")
public class AdministradorController {

    @Autowired
    private AdministradorService administradorService;

    @GetMapping
    public List<Administrador> getAll() {
        return administradorService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Administrador> getById(@PathVariable Long id) {
        return administradorService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Administrador create(@RequestBody Administrador administrador) {
        return administradorService.save(administrador);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Administrador> update(@PathVariable Long id, @RequestBody Administrador administrador) {
        return administradorService.findById(id)
                .map(existing -> {
                    existing.setNombre(administrador.getNombre());
                    return ResponseEntity.ok(administradorService.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (administradorService.findById(id).isPresent()) {
            administradorService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
