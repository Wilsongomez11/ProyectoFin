package com.example.ProyectoFinal.Service;


import com.example.ProyectoFinal.Modelo.Mesa;
import com.example.ProyectoFinal.Repository.MesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesaService {

    @Autowired
    private MesaRepository mesaRepository;

    public Mesa save(Mesa mesa) {
        return mesaRepository.save(mesa);
    }

    public List<Mesa> findAll() {
        return mesaRepository.findAll();
    }

    public Mesa findById(Long id) {
        return mesaRepository.findById(id).orElse(null);
    }

    public Mesa update(Long id, Mesa nueva) {
        return mesaRepository.findById(id).map(original -> {
            original.setNumero(nueva.getNumero());
            original.setEstado(nueva.getEstado());
            return mesaRepository.save(original);
        }).orElse(null);
    }

    public Mesa actualizarEstado(Long id, String estado) {
        Mesa mesa = findById(id);
        if (mesa == null) throw new RuntimeException("Mesa no encontrada");
        mesa.setEstado(estado);
        return mesaRepository.save(mesa);
    }
}
