package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidades.GameSession;
import com.tallerwebi.dominio.entidades.Jugador;

import java.util.List;

public interface ServicioJuego {
    GameSession iniciarPartida();
    List<SessionMonster> getMonstruos();

    GameSession crearNuevaMazmorra();

    void reiniciarMazmorra();

    Jugador getJugador();
    String atacar(int orden);
    String defender();
    String usarPocion();
    Object getSession();


    void endSession(GameSession current);


}