package com.inventario.inventario.service;

import com.inventario.inventario.model.Producto;

public interface InventarioService {
    Producto verificarYCrearDesdeProducto(Long id);

    Producto buscarInventario(Long id);
}