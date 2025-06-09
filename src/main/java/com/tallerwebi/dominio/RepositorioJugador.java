package com.tallerwebi.dominio;

public interface RepositorioJugador {
    Jugador findById(Long id);
    void save(Jugador j);
}
