package com.example.ProyectoFinal.Service;

import com.example.ProyectoFinal.Modelo.Administrador;
import java.util.List;

import com.example.ProyectoFinal.Repository.AdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AdministradorService {

    @Autowired
    private AdministradorRepository administradorRepository;

    public List<Administrador> findAll() {
        return administradorRepository.findAll();
    }

    public Optional<Administrador> login(String username, String password) {
        return administradorRepository.findByUsernameAndPassword(username, password);
    }

    public Optional<Administrador> findById(Long id) {
        return administradorRepository.findById(id);
    }

    public Administrador save(Administrador administrador) {
        return administradorRepository.save(administrador);
    }

    public Optional<Administrador> update(Long id, Administrador administradorData) {
        return administradorRepository.findById(id)
                .map(existing -> {
                    existing.setNombre(administradorData.getNombre());
                    existing.setUsername(administradorData.getUsername());
                    existing.setPassword(administradorData.getPassword());
                    existing.setCargo(administradorData.getCargo());
                    return administradorRepository.save(existing);
                });
    }


    public boolean deleteById(Long id) {
        return administradorRepository.findById(id).map(existing -> {
            administradorRepository.deleteById(id);
            return true;
        }).orElse(false);
    }
}

