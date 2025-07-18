package com.inventario.inventario.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {
    @Id
    private Long id;

    @Column(nullable = false)
    private Integer cantidad;
}
