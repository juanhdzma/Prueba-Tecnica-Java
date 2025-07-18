package com.inventario.inventario.service;

import java.time.LocalDateTime;

import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

import com.inventario.inventario.model.Compra;
import com.inventario.inventario.model.CompraRequestDTO;
import com.inventario.inventario.model.Producto;
import com.inventario.inventario.model.ProductoResponseDTO;
import com.inventario.inventario.repository.CompraRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CompraServiceImpl implements CompraService {

    private final CompraRepository compraRepository;
    private final InventarioService inventarioService;

    public CompraServiceImpl(CompraRepository compraRepository, InventarioService inventarioService) {
        this.compraRepository = compraRepository;
        this.inventarioService = inventarioService;
    }

    @Override
    @Retryable(value = { Exception.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public Compra procesarCompra(CompraRequestDTO dto) {
        log.info("Procesando compra: {}", dto);
        ProductoResponseDTO producto = inventarioService.getProducto(dto.getId());
        if (producto == null) {
            throw new RuntimeException("Producto no encontrado en el sistema externo");
        }

        Producto productoInventario = inventarioService.buscarInventario(dto.getId());
        if (productoInventario == null) {
            throw new RuntimeException("Producto no encontrado en inventario local");
        }

        if (productoInventario.getCantidad() < dto.getCantidad()) {
            throw new RuntimeException("Cantidad insuficiente en inventario. Disponible: "
                    + productoInventario.getCantidad());
        }

        int nuevaCantidad = productoInventario.getCantidad() - dto.getCantidad();

        Producto productoActualizado = Producto.builder()
                .id(producto.id())
                .cantidad(nuevaCantidad)
                .build();

        inventarioService.guardarInventario(productoActualizado);

        Compra compra = Compra.builder()
                .idArticulo(producto.id())
                .nombreArticulo(producto.nombre())
                .cantidad(dto.getCantidad())
                .precioUnidad(producto.precio())
                .valorTotal(producto.precio() * dto.getCantidad())
                .fechaHora(LocalDateTime.now())
                .build();

        Compra guardada = compraRepository.save(compra);
        log.info("🛒 Compra registrada: {}", guardada);
        return guardada;
    }
}
