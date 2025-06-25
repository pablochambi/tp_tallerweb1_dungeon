package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidades.GameSession;
import com.tallerwebi.dominio.entidades.SessionHero;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.entidades.SessionMonster;

import java.util.List;

public interface ServicioJuego {

    GameSession iniciarPartida();
    GameSession crearNuevaMazmorra();
    void reiniciarMazmorra();

    GameSession getSession();

    Usuario getUsuario();

    List<SessionMonster> getMonstruos();

    List<SessionHero> getHeroesDeSesion();

    /**
     * Un héroe ataca a un monstruo.
     * @param heroOrden    la posición del héroe (SessionHero.orden)
     * @param monsterOrden la posición del monstruo (SessionMonster.orden)
     * @return un texto con el resultado del turno
     */
    String atacar(int heroOrden, int monsterOrden);

    String defender(int heroOrden);


    String usarPocion(int heroOrden);

    void endSession(GameSession current);
}