package com.example.ProyectoFinal.Controller;

import com.example.ProyectoFinal.Modelo.Mesero;
import com.example.ProyectoFinal.Service.MeseroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/meseros")
@CrossOrigin(origins = "*")
public class MeseroController {

    @Autowired
    private MeseroService meseroService;

    @GetMapping
    public List<Mesero> listarMeseros() {
        return meseroService.listarMeseros();
    }

    @PostMapping("/registrar")
    public ResponseEntity<Mesero> registrarMesero(@RequestBody Mesero mesero) {
        return ResponseEntity.ok(meseroService.registrarMesero(mesero));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginMesero(@RequestBody Mesero credenciales) {
        Optional<Mesero> mesero = meseroService.loginMesero(credenciales.getUsername(), credenciales.getPassword());
        return mesero.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(401).body("Credenciales incorrectas"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mesero> actualizar(@PathVariable Long id, @RequestBody Mesero m) {
        return ResponseEntity.ok(meseroService.actualizar(id, m));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        meseroService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
