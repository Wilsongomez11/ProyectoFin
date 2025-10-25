package com.example.ProyectoFinal.Controller;

import com.example.ProyectoFinal.Modelo.Administrador;
import com.example.ProyectoFinal.Modelo.Producto;
import com.example.ProyectoFinal.Modelo.ProductoDTO;
import com.example.ProyectoFinal.Modelo.Proveedor;
import com.example.ProyectoFinal.Service.AdministradorService;
import com.example.ProyectoFinal.Service.ProductoService;
import com.example.ProyectoFinal.Service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProveedorService proveedorService;

    @Autowired
    private AdministradorService administradorService;

    //  Fabricar producto (aumenta stock y descuenta insumos)
    @PutMapping("/fabricar/{id}/{cantidad}")
    public ResponseEntity<String> fabricar(@PathVariable Long id, @PathVariable int cantidad) {
        try {
            productoService.fabricarProducto(id, cantidad);
            return ResponseEntity.ok("✅ Se fabricaron " + cantidad +
                    " unidades y se descontaron los insumos correspondientes.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("❌ Error al fabricar: " + e.getMessage());
        }
    }

    // Listar productos
    @GetMapping
    public List<Producto> getAll() {
        return productoService.findAll();
    }

    // Buscar producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getById(@PathVariable Long id) {
        return productoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //  Crear producto
    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProductoDTO dto) {
        try {
            Producto producto = productoService.crearProductoDesdeDTO(dto);
            return ResponseEntity.ok(producto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body("Error al crear producto: " + e.getMessage());
        }
    }

    //  Actualizar producto existente
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ProductoDTO dto) {
        try {
            Producto actualizado = productoService.actualizarProductoDesdeDTO(id, dto);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body("Error al actualizar producto: " + e.getMessage());
        }
    }

    //  Eliminar producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (productoService.findById(id).isPresent()) {
            productoService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

