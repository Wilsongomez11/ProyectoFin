package com.example.ProyectoFinal.Service;

import com.example.ProyectoFinal.Modelo.Mesero;
import com.example.ProyectoFinal.Repository.MeseroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MeseroService {

    @Autowired
    private MeseroRepository repo;

    public List<Mesero> listarMeseros() { return repo.findAll(); }

    public Mesero registrarMesero(Mesero mesero) { return repo.save(mesero); }

    public Optional<Mesero> loginMesero(String username, String password) {
        return repo.findByUsernameAndPassword(username, password);
    }

    public Mesero actualizar(Long id, Mesero m) {
        Mesero ex = repo.findById(id).orElseThrow();
        ex.setNombre(m.getNombre());
        ex.setTelefono(m.getTelefono());
        ex.setCorreo(m.getCorreo());
        ex.setUsername(m.getUsername());
        ex.setPassword(m.getPassword());
        return repo.save(ex);
    }

    public void eliminar(Long id) { repo.deleteById(id); }
}

