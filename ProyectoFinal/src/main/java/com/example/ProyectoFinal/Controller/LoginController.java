package com.example.ProyectoFinal.Controller;

import com.example.ProyectoFinal.Modelo.LoginRequest;
import com.example.ProyectoFinal.Modelo.LoginResponse;
import com.example.ProyectoFinal.Modelo.Administrador;
import com.example.ProyectoFinal.Modelo.Mesero;
import com.example.ProyectoFinal.Modelo.Pizzero;
import com.example.ProyectoFinal.Repository.AdministradorRepository;
import com.example.ProyectoFinal.Repository.MeseroRepository;
import com.example.ProyectoFinal.Repository.PizzeroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "*")
public class LoginController {

    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private MeseroRepository meseroRepository;

    @Autowired
    private PizzeroRepository pizzeroRepository;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        // 👨‍💼 Buscar administrador
        Optional<Administrador> admin = administradorRepository.findByUsernameAndPassword(username, password);
        if (admin.isPresent()) {
            Administrador a = admin.get();
            return ResponseEntity.ok(new LoginResponse(
                    a.getId(),
                    a.getNombre(),
                    a.getUsername(),
                    "ADMINISTRADOR"
            ));
        }

        // 🍽️ Buscar mesero
        Optional<Mesero> mesero = meseroRepository.findByUsernameAndPassword(username, password);
        if (mesero.isPresent()) {
            Mesero m = mesero.get();
            return ResponseEntity.ok(new LoginResponse(
                    m.getId(),
                    m.getNombre(),
                    m.getUsername(),
                    "MESERO"
            ));
        }

        // 🍕 Buscar pizzero
        Optional<Pizzero> pizzero = pizzeroRepository.findByUsernameAndPassword(username, password);
        if (pizzero.isPresent()) {
            Pizzero p = pizzero.get();
            return ResponseEntity.ok(new LoginResponse(
                    p.getId(),
                    p.getNombre(),
                    p.getUsername(),
                    "PIZZERO"
            ));
        }


        return ResponseEntity.status(401).body("Credenciales incorrectas");
    }
}
