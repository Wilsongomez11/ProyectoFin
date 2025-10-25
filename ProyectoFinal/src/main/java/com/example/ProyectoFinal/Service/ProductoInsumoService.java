    package com.example.ProyectoFinal.Service;

    import com.example.ProyectoFinal.Modelo.Insumo;
    import com.example.ProyectoFinal.Modelo.ProductoInsumo;
    import com.example.ProyectoFinal.Repository.ProductoInsumoRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.util.List;

    @Service
    public class ProductoInsumoService {

        @Autowired
        private ProductoInsumoRepository productoInsumoRepository;

        @Autowired
        private InsumoService insumoService;

        public List<ProductoInsumo> findByProducto(Long productoId) {
            return productoInsumoRepository.findByProductoId(productoId);
        }

        public ProductoInsumo save(ProductoInsumo pi) {
            return productoInsumoRepository.save(pi);
        }

        public void descontarInsumos(Long productoId, int cantidadFabricada) {
            List<ProductoInsumo> relaciones = productoInsumoRepository.findByProductoId(productoId);

            for (ProductoInsumo relacion : relaciones) {
                Insumo insumo = relacion.getInsumo();
                double cantidadTotal = relacion.getCantidadUsada() * cantidadFabricada;
                insumoService.descontarStock(insumo.getId(), cantidadTotal);
            }
        }
    }
