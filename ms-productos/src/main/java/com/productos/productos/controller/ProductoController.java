package com.productos.productos.controller;

import com.productos.productos.response.ApiResponse;
import com.productos.productos.model.ProductoRequestDTO;
import com.productos.productos.model.ProductoResponseDTO;
import com.productos.productos.service.ProductoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping("/producto")
    public ResponseEntity<ApiResponse<ProductoResponseDTO>> crearProducto(
            @RequestBody @Valid ProductoRequestDTO request) {
        ProductoResponseDTO producto = productoService.crearProducto(request);
        log.info("✅ Producto creado exitosamente: {}", producto);
        return ResponseEntity.ok(new ApiResponse<>(
                "Producto creado exitosamente",
                producto));
    }

    @GetMapping("/producto/{id}")
    public ResponseEntity<ApiResponse<ProductoResponseDTO>> obtenerProductoPorId(@PathVariable Long id) {
        ProductoResponseDTO producto = productoService.obtenerProductoPorId(id);
        log.info("📦 Producto encontrado: {}", producto);
        return ResponseEntity.ok(new ApiResponse<>("Producto obtenido correctamente", producto));
    }

    @GetMapping("/productos")
    public ResponseEntity<ApiResponse<List<ProductoResponseDTO>>> obtenerTodosLosProductos() {
        List<ProductoResponseDTO> productos = productoService.obtenerTodosLosProductos();
        log.info("📦 Productos obtenidos: {} encontrados", productos.size());
        return ResponseEntity.ok(new ApiResponse<>("Listado de productos obtenido correctamente", productos));
    }
}