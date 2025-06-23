package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidades.GameSession;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.entidades.SessionMonster;

import java.util.List;

public interface ServicioJuego {
    GameSession iniciarPartida();
    List<SessionMonster> getMonstruos();

    GameSession crearNuevaMazmorra();

    void reiniciarMazmorra();

    Usuario getUsuario();
    String atacar(int orden);
    String defender();
    String usarPocion();
    Object getSession();


    void endSession(GameSession current);


}