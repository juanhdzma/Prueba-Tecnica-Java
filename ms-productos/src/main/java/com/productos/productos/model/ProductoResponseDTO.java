package com.productos.productos.model;

public record ProductoResponseDTO(
        Long id,
        String nombre,
        Double precio,
        String descripcion) {
}