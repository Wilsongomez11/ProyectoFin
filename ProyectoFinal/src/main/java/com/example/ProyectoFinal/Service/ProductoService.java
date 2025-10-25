package com.example.ProyectoFinal.Service;

import com.example.ProyectoFinal.Modelo.Administrador;
import com.example.ProyectoFinal.Modelo.Producto;
import com.example.ProyectoFinal.Modelo.ProductoDTO;
import com.example.ProyectoFinal.Modelo.Proveedor;
import com.example.ProyectoFinal.Repository.AdministradorRepository;
import com.example.ProyectoFinal.Repository.ProductoRepository;
import com.example.ProyectoFinal.Repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private AdministradorRepository administradorRepository;

    // conexión con insumos
    @Autowired
    private ProductoInsumoService productoInsumoService;

    public Producto crearProductoDesdeDTO(ProductoDTO dto) {

        Administrador administrador = administradorRepository.findById(dto.getIdAdministrador())
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));

        Proveedor proveedor = null;
        if (dto.getIdProveedor() != null && dto.getIdProveedor() > 0) {
            proveedor = proveedorRepository.findById(dto.getIdProveedor())
                    .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        }

        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setPrecio(dto.getPrecio());
        producto.setCantidad(dto.getCantidad());
        producto.setAdministrador(administrador);
        producto.setProveedor(proveedor);

        return productoRepository.save(producto);
    }


    // Listar todos los productos

    public List<Producto> findAll() {
        return productoRepository.findAll();
    }


    // Buscar producto por ID
    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }

    // Guardar producto
    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }


    // Eliminar producto

    public void deleteById(Long id) {
        productoRepository.deleteById(id);
    }


    // Actualizar producto
    public Producto actualizarProductoDesdeDTO(Long id, ProductoDTO dto) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Proveedor proveedor = null;
        if (dto.getIdProveedor() != null && dto.getIdProveedor() > 0) {
            proveedor = proveedorRepository.findById(dto.getIdProveedor())
                    .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        }

        Administrador administrador = administradorRepository.findById(dto.getIdAdministrador())
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));

        producto.setNombre(dto.getNombre());
        producto.setPrecio(dto.getPrecio());
        producto.setCantidad(dto.getCantidad());
        producto.setProveedor(proveedor);
        producto.setAdministrador(administrador);

        return productoRepository.save(producto);
    }


    //  NUEVO: Fabricar producto y descontar insumos

    public void fabricarProducto(Long productoId, int cantidadFabricada) {
        // Verificar que el producto exista
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Descontar del stock los insumos asociados
        productoInsumoService.descontarInsumos(productoId, cantidadFabricada);

        // (Opcional) aumentar el stock del producto fabricado
        producto.setCantidad(producto.getCantidad() + cantidadFabricada);
        productoRepository.save(producto);
    }
}



