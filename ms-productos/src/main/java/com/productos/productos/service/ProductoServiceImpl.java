package com.productos.productos.service;

import com.productos.productos.model.ProductoRequestDTO;
import com.productos.productos.model.ProductoResponseDTO;
import com.productos.productos.model.Producto;
import com.productos.productos.repository.ProductoRepository;
// import com.productos.productos.service.ProductoService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    @Transactional
    public ProductoResponseDTO crearProducto(ProductoRequestDTO request) {
        try {
            Producto producto = Producto.builder()
                    .nombre(request.nombre())
                    .precio(request.precio())
                    .descripcion(request.descripcion())
                    .build();

            Producto guardado = productoRepository.save(producto);

            return new ProductoResponseDTO(
                    guardado.getId(),
                    guardado.getNombre(),
                    guardado.getPrecio(),
                    guardado.getDescripcion());

        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el producto: " + e.getMessage(), e);
        }
    }
}