package com.example.ProyectoFinal.Controller;

import com.example.ProyectoFinal.Modelo.DevolucionRequest;
import com.example.ProyectoFinal.Modelo.Pedido;
import com.example.ProyectoFinal.Repository.PedidoRepository;
import com.example.ProyectoFinal.Service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/pedidos")
public class PedidoController {


    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<List<Pedido>> getAll() {
        List<Pedido> lista = pedidoService.findAll();
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getById(@PathVariable Long id) {
        return pedidoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pedido> crearPedido(@RequestBody Pedido pedido) {
        return ResponseEntity.ok(pedidoService.crearPedido(pedido));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Pedido> actualizarEstado(
            @PathVariable Long id,
            @RequestParam String estado
    ) {
        return ResponseEntity.ok(pedidoService.actualizarEstado(id, estado));
    }

    @PutMapping("/{id}/devolver")
    public ResponseEntity<Void> devolverPedido(
            @PathVariable Long id,
            @RequestBody DevolucionRequest request
    ) {
        pedidoService.devolverPedidoParcial(id, request);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pedidoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> actualizarPedido(
            @PathVariable Long id,
            @RequestBody Pedido pedidoActualizado
    ) {
        return ResponseEntity.ok(pedidoService.actualizarPedido(id, pedidoActualizado));
    }

    @GetMapping("/mesas/estado")
    public ResponseEntity<Map<Integer, String>> getEstadoMesas() {
        Map<Integer, String> estadoMesas = pedidoService.getEstadoMesas();
        return ResponseEntity.ok(estadoMesas);
    }
}