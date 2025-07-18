package com.inventario.inventario.service;

import com.inventario.inventario.model.CompraRequestDTO;
import com.inventario.inventario.model.Compra;

public interface CompraService {
    Compra procesarCompra(CompraRequestDTO dto);
}