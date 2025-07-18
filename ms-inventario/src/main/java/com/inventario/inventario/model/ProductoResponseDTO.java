package com.inventario.inventario.model;

public record ProductoResponseDTO(
        Long id,
        String nombre,
        Double precio,
        String descripcion) {
}