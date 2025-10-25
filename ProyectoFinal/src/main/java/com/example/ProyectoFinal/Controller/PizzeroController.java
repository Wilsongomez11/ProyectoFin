package com.example.ProyectoFinal.Controller;

import com.example.ProyectoFinal.Modelo.Pizzero;
import com.example.ProyectoFinal.Service.PizzeroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pizzeros")
@CrossOrigin(origins = "*")
public class PizzeroController {

    @Autowired
    private PizzeroService pizzeroService;

    @GetMapping
    public List<Pizzero> listar() { return pizzeroService.listar(); }

    @PostMapping("/registrar")
    public ResponseEntity<Pizzero> registrar(@RequestBody Pizzero p) { return ResponseEntity.ok(pizzeroService.registrar(p)); }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Pizzero c) {
        Optional<Pizzero> p = pizzeroService.login(c.getUsername(), c.getPassword());
        return p.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(401).body("Credenciales incorrectas"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pizzero> actualizar(@PathVariable Long id, @RequestBody Pizzero p) {
        return ResponseEntity.ok(pizzeroService.actualizar(id, p));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pizzeroService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
