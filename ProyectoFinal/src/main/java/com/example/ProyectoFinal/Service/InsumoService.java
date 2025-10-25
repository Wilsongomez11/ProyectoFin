package com.example.ProyectoFinal.Service;

import com.example.ProyectoFinal.Modelo.Insumo;
import com.example.ProyectoFinal.Repository.InsumoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InsumoService {

    @Autowired
    private InsumoRepository insumoRepository;

    public List<Insumo> findAll() {
        return insumoRepository.findAll();
    }

    public Optional<Insumo> findById(Long id) {
        return insumoRepository.findById(id);
    }

    public Insumo save(Insumo insumo) {
        return insumoRepository.save(insumo);
    }

    public void deleteById(Long id) {
        insumoRepository.deleteById(id);
    }

    public boolean descontarStock(Long id, double cantidadUsada) {
        Optional<Insumo> insumoOpt = insumoRepository.findById(id);
        if (insumoOpt.isPresent()) {
            Insumo insumo = insumoOpt.get();
            double nuevoStock = insumo.getCantidadActual() - cantidadUsada;
            if (nuevoStock < 0) return false;
            insumo.setCantidadActual(nuevoStock);
            insumoRepository.save(insumo);
            return true;
        }
        return false;
    }

    public List<Insumo> getInsumosBajoStock() {
        return insumoRepository.findAll()
                .stream()
                .filter(i -> i.getCantidadActual() <= i.getCantidadMinima())
                .toList();
    }
}
