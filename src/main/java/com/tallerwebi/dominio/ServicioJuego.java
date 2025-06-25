package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidades.GameSession;
import com.tallerwebi.dominio.entidades.SessionHero;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.entidades.SessionMonster;

import java.util.List;

public interface ServicioJuego {

    GameSession iniciarPartida(Usuario u);
    GameSession crearNuevaMazmorra(Usuario u);
    void reiniciarMazmorra(Usuario u);
    GameSession getSession(Usuario u);
    Usuario getUsuario(Usuario u);

    GameSession getSession();

    Usuario getUsuario();

    List<SessionMonster> getMonstruos(Usuario u);
    List<SessionHero>    getHeroesDeSesion(Usuario u);
    String atacar(Usuario u, int heroOrden, int monsterOrden);
    String defender(Usuario u, int heroOrden);
    String usarPocion(Usuario u, int heroOrden);
    void siguienteMazmorra(Usuario u);
    void endSession(GameSession current);
}