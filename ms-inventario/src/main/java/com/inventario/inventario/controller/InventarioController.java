package com.inventario.inventario.controller;

import com.inventario.inventario.model.Producto;
import com.inventario.inventario.model.ActualizarCantidadRequestDTO;
import com.inventario.inventario.model.Compra;
import com.inventario.inventario.model.CompraRequestDTO;
import com.inventario.inventario.response.ApiResponse;
import com.inventario.inventario.service.CompraService;
import com.inventario.inventario.service.InventarioService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class InventarioController {

    private final InventarioService inventarioService;
    private final CompraService compraService;

    public InventarioController(CompraService compraService, InventarioService inventarioService) {
        this.inventarioService = inventarioService;
        this.compraService = compraService;
    }

    @GetMapping("/producto/{id}")
    public ResponseEntity<ApiResponse<?>> consultarInventario(@PathVariable Long id) {
        Producto resultado = inventarioService.verificarYCrearDesdeProducto(id);

        if (resultado == null) {
            Map<String, String> errorData = Map.of("Producto", "Producto no existe en el sistema");
            return ResponseEntity.badRequest().body(new ApiResponse<>("Proceso no completado", errorData));
        }

        return ResponseEntity.ok(new ApiResponse<>("Producto en inventario encontrado o creado", resultado));
    }

    @PatchMapping("/producto/actualizar_cantidad")
    public ResponseEntity<ApiResponse<?>> actualizarCantidad(
            @RequestBody @Valid ActualizarCantidadRequestDTO dto) {
        Producto producto = inventarioService.buscarInventario(dto.id());

        if (producto == null) {
            producto = inventarioService.verificarYCrearDesdeProducto(dto.id());
            if (producto == null) {
                Map<String, String> errorData = Map.of("Producto", "Producto no existe en el sistema");
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Producto no existe en el sistema", errorData));
            }
        }

        producto.setCantidad(dto.cantidad());
        Producto actualizado = inventarioService.guardarInventario(producto);
        return ResponseEntity.ok(new ApiResponse<>("Cantidad actualizada correctamente", actualizado));
    }

    @PostMapping("/compra")
    public ResponseEntity<ApiResponse<Compra>> registrarCompra(@RequestBody @Valid CompraRequestDTO dto) {
        Compra compra = compraService.procesarCompra(dto);
        return ResponseEntity.ok(new ApiResponse<>("Compra registrada correctamente", compra));
    }
}