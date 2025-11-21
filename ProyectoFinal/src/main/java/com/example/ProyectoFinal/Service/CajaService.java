package com.example.ProyectoFinal.Service;

import com.example.ProyectoFinal.Modelo.Caja;
import com.example.ProyectoFinal.Modelo.MovimientoCaja;
import com.example.ProyectoFinal.Repository.CajaRepository;
import com.example.ProyectoFinal.Repository.MovimientoCajaRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CajaService {

    @Autowired
    private CajaRepository cajaRepository;

    @Autowired
    private MovimientoCajaRepository movimientoRepo;

    public Caja obtenerCajaDelDia() {
        LocalDate fechaHoy = LocalDate.now();

        Optional<Caja> cajaOpt = cajaRepository.findFirstByFechaOrderByIdDesc(fechaHoy);

        if (cajaOpt.isPresent()) {
            return cajaOpt.get();
        }

        Caja nuevaCaja = new Caja();
        nuevaCaja.setFecha(fechaHoy);
        nuevaCaja.setIngresos(0.0);
        nuevaCaja.setEgresos(0.0);
        nuevaCaja.setBalance(0.0);
        return cajaRepository.save(nuevaCaja);
    }

    public List<Caja> findAll() {
        return cajaRepository.findAll();
    }

    public Caja save(Caja caja) {
        return cajaRepository.save(caja);
    }

    @Transactional
    public void registrarIngreso(Double monto, String descripcion, MovimientoCaja mov) {

        Caja caja = obtenerCajaDelDia();

        mov.setTipo("ingreso");
        mov.setMonto(monto);
        mov.setCaja(caja);
        mov.setFecha(LocalDateTime.now());

        movimientoRepo.save(mov);

        caja.setIngresos(caja.getIngresos() + monto);
        caja.setBalance(caja.getIngresos() - caja.getEgresos());

        cajaRepository.save(caja);
    }
}

