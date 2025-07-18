package com.productos.productos.controller;

import com.productos.productos.response.ApiResponse;
import com.productos.productos.model.ProductoRequestDTO;
import com.productos.productos.model.ProductoResponseDTO;
import com.productos.productos.service.ProductoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productos")
@Slf4j
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductoResponseDTO>> crearProducto(
            @RequestBody @Valid ProductoRequestDTO request) {
        ProductoResponseDTO producto = productoService.crearProducto(request);
        log.info("✅ Producto creado exitosamente: {}", producto);
        return ResponseEntity.ok(new ApiResponse<>(
                "Producto creado exitosamente",
                producto));
    }
}