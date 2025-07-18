package com.productos.productos.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductoRequestDTO(
                @NotBlank(message = "El nombre es obligatorio") String nombre,
                @NotBlank(message = "La descripción es obligatoria") String descripcion,
                @NotNull(message = "El precio es obligatorio") Double precio) {
}