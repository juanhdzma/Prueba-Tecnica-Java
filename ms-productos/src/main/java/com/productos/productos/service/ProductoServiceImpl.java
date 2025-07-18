package com.productos.productos.service;

import com.productos.productos.model.ProductoRequestDTO;
import com.productos.productos.model.ProductoResponseDTO;
import com.productos.productos.model.Producto;
import com.productos.productos.repository.ProductoRepository;
import com.productos.productos.response.ProductoNoEncontradoException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

            return mapToDTO(guardado);

        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el producto: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponseDTO obtenerProductoPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException("Producto no encontrado con ID " + id));
        return mapToDTO(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> obtenerTodosLosProductos() {
        return productoRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private ProductoResponseDTO mapToDTO(Producto producto) {
        return new ProductoResponseDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getDescripcion());
    }
}