
package com.inventario.inventario.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ActualizarCantidadRequestDTO(
        @NotNull Long id,
        @NotNull @Min(0) Integer cantidad) {
}
