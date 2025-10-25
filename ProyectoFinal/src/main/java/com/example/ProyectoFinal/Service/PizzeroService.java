package com.example.ProyectoFinal.Service;

import com.example.ProyectoFinal.Modelo.Pizzero;
import com.example.ProyectoFinal.Repository.PizzeroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PizzeroService {

    @Autowired
    private PizzeroRepository repo;

    public List<Pizzero> listar() { return repo.findAll(); }

    public Pizzero registrar(Pizzero p) { return repo.save(p); }

    public Optional<Pizzero> login(String u, String p) { return repo.findByUsernameAndPassword(u, p); }

    public Pizzero actualizar(Long id, Pizzero p) {
        Pizzero ex = repo.findById(id).orElseThrow();
        ex.setNombre(p.getNombre());
        ex.setTelefono(p.getTelefono());
        ex.setDireccion(p.getDireccion());
        ex.setUsername(p.getUsername());
        ex.setPassword(p.getPassword());
        return repo.save(ex);
    }

    public void eliminar(Long id) { repo.deleteById(id); }
}


