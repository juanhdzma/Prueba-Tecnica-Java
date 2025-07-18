package com.productos.productos.service;

import com.productos.productos.model.ProductoRequestDTO;
import com.productos.productos.model.ProductoResponseDTO;

import java.util.List;

public interface ProductoService {
    ProductoResponseDTO crearProducto(ProductoRequestDTO request);

    ProductoResponseDTO obtenerProductoPorId(Long id);

    List<ProductoResponseDTO> obtenerTodosLosProductos();
}