package com.inventario.inventario.service;

import com.inventario.inventario.model.MSProductoResponseDTO;
import com.inventario.inventario.model.Producto;
import com.inventario.inventario.model.ProductoResponseDTO;
import com.inventario.inventario.repository.InventarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Service
@Slf4j
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepository;
    private final RestTemplate restTemplate;

    public InventarioServiceImpl(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public Producto buscarInventario(Long id) {
        return inventarioRepository.findById(id).orElse(null);
    }

    @Override
    public Producto guardarInventario(Producto producto) {
        return inventarioRepository.save(producto);
    }

    @Override
    public Producto verificarYCrearDesdeProducto(Long id) {
        ProductoResponseDTO producto = consultarProducto(id);
        if (producto == null) {
            log.warn("🚫 Producto con ID {} no encontrado en el MS de productos", id);
            return null;
        }

        Producto nuevoProducto = Producto.builder()
                .id(producto.id())
                .cantidad(0)
                .build();

        Producto guardado = inventarioRepository.save(nuevoProducto);
        log.info("✅ Producto registrado en inventario con cantidad 0: {}", guardado);
        return guardado;
    }

    @SuppressWarnings("null")
    private ProductoResponseDTO consultarProducto(Long id) {
        String url = "http://localhost:8080/producto/" + id;

        // Autenticación básica codificada
        String username = "admin";
        String password = "admin123";
        String auth = username + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedAuth);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<MSProductoResponseDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    MSProductoResponseDTO.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return (response.getBody() != null) ? response.getBody().data() : null;
            }

            log.warn("⚠️ Respuesta no satisfactoria al consultar producto: {}", response.getStatusCode());
            return null;

        } catch (Exception e) {
            log.error("❌ Error durante la consulta al MS de productos: {}", e.getMessage());
            return null;
        }
    }
}