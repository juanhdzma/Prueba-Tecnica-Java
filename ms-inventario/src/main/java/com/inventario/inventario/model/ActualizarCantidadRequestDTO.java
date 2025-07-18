package com.inventario.inventario.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ActualizarCantidadRequestDTO(
                @NotNull(message = "El ID del producto es obligatorio") Long id,
                @NotNull(message = "La cantidad es obligatoria") @Min(value = 0, message = "La cantidad no puede ser negativa") Integer cantidad) {
}