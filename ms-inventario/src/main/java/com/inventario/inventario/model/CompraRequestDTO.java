package com.inventario.inventario.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompraRequestDTO {

        @NotNull(message = "El ID del producto es obligatorio")
        private Long id;

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        private Integer cantidad;
}