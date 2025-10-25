package com.example.ProyectoFinal.Controller;

import com.example.ProyectoFinal.Modelo.Administrador;
import com.example.ProyectoFinal.Modelo.LoginRequest;
import com.example.ProyectoFinal.Service.AdministradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/administrador")
public class AdministradorController {

    @Autowired
    private AdministradorService administradorService;

    @GetMapping
    public List<Administrador> getAll() {
        return administradorService.findAll();
    }

    @PostMapping
    public Administrador create(@RequestBody Administrador administrador) {
        return administradorService.save(administrador);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Administrador administrador) {
        try {
            Optional<Administrador> updated = administradorService.findById(id)
                    .map(existing -> {
                        existing.setNombre(administrador.getNombre());
                        existing.setUsername(administrador.getUsername());
                        existing.setPassword(administrador.getPassword());
                        existing.setCargo(administrador.getCargo());
                        return administradorService.save(existing);
                    });

            if (updated.isPresent()) {
                return ResponseEntity.ok(updated.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Administrador no encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar: " + e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (administradorService.findById(id).isPresent()) {
            administradorService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Administrador> adminOpt = administradorService.login(request.getUsername(), request.getPassword());

        if (adminOpt.isPresent()) {
            return ResponseEntity.ok(adminOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }
}
