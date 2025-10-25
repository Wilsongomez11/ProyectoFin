package com.example.ProyectoFinal.Controller;

import com.example.ProyectoFinal.Modelo.Caja;
import com.example.ProyectoFinal.Service.CajaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cajas")
public class CajaController {

    @Autowired
    private CajaService cajaService;

    @GetMapping
    public List<Caja> getAll() {
        return cajaService.findAll();
    }

    @PostMapping
    public Caja create(@RequestBody Caja caja) {
        return cajaService.save(caja);
    }
}
