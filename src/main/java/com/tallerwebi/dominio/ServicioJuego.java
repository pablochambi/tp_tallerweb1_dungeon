package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioJuego {
    GameSession iniciarPartida();
    List<SessionMonster> getMonstruos();
    Jugador getJugador();
    String atacar(int orden);
    String defender();
    String usarPocion();
    Object getSession();


    void endSession(GameSession current);


}