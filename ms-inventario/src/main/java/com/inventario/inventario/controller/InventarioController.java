package com.inventario.inventario.controller;

import com.inventario.inventario.model.Producto;
import com.inventario.inventario.response.ApiResponse;
import com.inventario.inventario.service.InventarioService;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
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
}