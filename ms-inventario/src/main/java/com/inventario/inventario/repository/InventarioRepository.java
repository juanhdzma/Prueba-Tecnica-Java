package com.inventario.inventario.repository;

import com.inventario.inventario.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventarioRepository extends JpaRepository<Producto, Long> {
}
