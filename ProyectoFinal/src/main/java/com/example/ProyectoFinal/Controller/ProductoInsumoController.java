package com.example.ProyectoFinal.Controller;

import com.example.ProyectoFinal.Modelo.ProductoInsumo;
import com.example.ProyectoFinal.Service.ProductoInsumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/producto-insumos")
public class ProductoInsumoController {

    @Autowired
    private ProductoInsumoService productoInsumoService;

    //  Obtener insumos por producto
    @GetMapping("/{productoId}")
    public ResponseEntity<List<ProductoInsumo>> getInsumosByProducto(@PathVariable Long productoId) {
        List<ProductoInsumo> lista = productoInsumoService.findByProducto(productoId);
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    //  Crear relación producto-insumo (receta)
    @PostMapping
    public ResponseEntity<ProductoInsumo> create(@RequestBody ProductoInsumo productoInsumo) {
        ProductoInsumo saved = productoInsumoService.save(productoInsumo);
        return ResponseEntity.ok(saved);
    }
}
