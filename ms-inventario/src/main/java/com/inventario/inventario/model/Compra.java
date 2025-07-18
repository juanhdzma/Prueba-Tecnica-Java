package com.inventario.inventario.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "compras")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Compra {

    @Id
    private Long idArticulo;

    @Column(nullable = false)
    private String nombreArticulo;

    @Column(nullable = false)
    private Double precioUnidad;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Double valorTotal;

    @Column(nullable = false)
    private LocalDateTime fechaHora;
}