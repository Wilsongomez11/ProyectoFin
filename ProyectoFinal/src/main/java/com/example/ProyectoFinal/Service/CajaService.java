package com.example.ProyectoFinal.Service;

import com.example.ProyectoFinal.Modelo.Caja;
import com.example.ProyectoFinal.Repository.CajaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CajaService {
    @Autowired
    private CajaRepository cajaRepository;

    public List<Caja> findAll() {
        return cajaRepository.findAll();
    }

    public Caja save(Caja caja) {
        return cajaRepository.save(caja);
    }
}
