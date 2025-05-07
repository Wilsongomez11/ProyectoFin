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

    public Optional<Administrador> findById(Long id) {
        return administradorRepository.findById(id);
    }

    public Administrador save(Administrador administrador) {
        return administradorRepository.save(administrador);
    }

    public void deleteById(Long id) {
        administradorRepository.deleteById(id);
    }
}

