package com.inventario.inventario.repository;

import com.inventario.inventario.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompraRepository extends JpaRepository<Compra, Long> {
}