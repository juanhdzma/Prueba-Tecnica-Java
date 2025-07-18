package com.inventario.inventario.service;

import com.inventario.inventario.model.Producto;
import com.inventario.inventario.model.ProductoResponseDTO;

public interface InventarioService {
    Producto verificarYCrearDesdeProducto(Long id);

    ProductoResponseDTO getProducto(Long id);

    Producto buscarInventario(Long id);

    Producto guardarInventario(Producto producto);
}