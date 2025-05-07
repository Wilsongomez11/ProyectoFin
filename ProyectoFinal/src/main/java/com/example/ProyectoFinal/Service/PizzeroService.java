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
    private PizzeroRepository pizzeroRepository;

    public List<Pizzero> findAll() {
        return pizzeroRepository.findAll();
    }

    public Optional<Pizzero> findById(Long id) {
        return pizzeroRepository.findById(id);
    }

    public Pizzero save(Pizzero pizzero) {
        return pizzeroRepository.save(pizzero);
    }

    public void deleteById(Long id) {
        pizzeroRepository.deleteById(id);
    }
}
