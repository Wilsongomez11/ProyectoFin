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
    private MeseroRepository meseroRepository;

    public List<Mesero> findAll() {
        return meseroRepository.findAll();
    }

    public Optional<Mesero> findById(Long id) {
        return meseroRepository.findById(id);
    }

    public Mesero save(Mesero mesero) {
        return meseroRepository.save(mesero);
    }

    public void deleteById(Long id) {
        meseroRepository.deleteById(id);
    }
}

