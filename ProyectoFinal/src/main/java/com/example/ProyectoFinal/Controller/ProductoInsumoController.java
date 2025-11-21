package com.example.ProyectoFinal.Controller;

import com.example.ProyectoFinal.Modelo.ProductoInsumo;
import com.example.ProyectoFinal.Repository.ProductoInsumoRepository;
import com.example.ProyectoFinal.Service.ProductoInsumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/producto-insumos")
public class ProductoInsumoController {

    @Autowired
    private ProductoInsumoRepository productoInsumoRepository;

    @Autowired
    private ProductoInsumoService productoInsumoService;

    // Obtener insumos por producto
    @GetMapping("/{productoId}")
    public ResponseEntity<List<ProductoInsumo>> getInsumosByProducto(@PathVariable Long productoId) {
        List<ProductoInsumo> lista = productoInsumoService.findByProducto(productoId);
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    // Crear relación producto–insumo
    @PostMapping
    public ResponseEntity<ProductoInsumo> create(@RequestBody ProductoInsumo productoInsumo) {
        ProductoInsumo saved = productoInsumoService.save(productoInsumo);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarRelacion(@PathVariable Long id) {
        if (!productoInsumoRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("La relación no existe");
        }

        productoInsumoRepository.deleteById(id);
        return ResponseEntity.ok("Relación eliminada correctamente");
    }
}
