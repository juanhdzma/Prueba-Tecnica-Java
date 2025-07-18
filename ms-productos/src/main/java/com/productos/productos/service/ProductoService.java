package com.productos.productos.service;

import com.productos.productos.model.ProductoRequestDTO;
import com.productos.productos.model.ProductoResponseDTO;

public interface ProductoService {
    ProductoResponseDTO crearProducto(ProductoRequestDTO request);
}
