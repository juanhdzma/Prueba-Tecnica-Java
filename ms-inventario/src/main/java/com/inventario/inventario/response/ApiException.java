package com.inventario.inventario.response;

public class ApiException extends RuntimeException {
    private final int status;

    public ApiException(int status, String message) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    // Error de formato (400)
    public static ApiException formatoInvalido(String campo) {
        return new ApiException(400, "Formato inválido para el campo: " + campo);
    }

    // Error al guardar en la base de datos
    public static ApiException errorGuardar(String entidad) {
        return new ApiException(500, "No se pudo guardar el/la " + entidad + " en la base de datos");
    }
}
