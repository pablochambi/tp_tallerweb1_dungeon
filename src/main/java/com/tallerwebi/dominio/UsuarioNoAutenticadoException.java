package com.tallerwebi.dominio;

public class UsuarioNoAutenticadoException extends RuntimeException {
    public UsuarioNoAutenticadoException() {
        super("Usuario no autenticado");
    }
}
