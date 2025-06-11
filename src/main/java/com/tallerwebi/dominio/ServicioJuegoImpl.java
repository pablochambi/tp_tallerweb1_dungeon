package com.tallerwebi.dominio;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServicioJuegoImpl implements ServicioJuego {


    @Autowired private RepositorioSession           repositorioSession;
    @Autowired private RepositorioSessionMonster    repositorioSessionMonster;
    @Autowired private RepositorioMonster           repositorioMonster;
    @Autowired private RepositorioJugador           repositorioJugador;

    //manejo de sesiones al arrancar partida
    public GameSession iniciarPartida() {
        GameSession session = repositorioSession.findActive();
        if (session == null) {
            Jugador j = repositorioJugador.findById(1L); //carga al jugador con id 1
            if (j == null) {
                j = new Jugador();
                j.setNombre("Héroe");
                j.setVida(100);
                j.setAtk(10);
                j.setDefensa(false);
                j.setOro(1000);
                repositorioJugador.save(j);
            }

            session = new GameSession(); //carga la gamesession con eljugador en sesion
            session.setJugador(j);
            repositorioSession.save(session);

            List<Monster> monsters = repositorioMonster.obtenerTodosLosMonstruos();
            for (Monster m : monsters) {
                repositorioSessionMonster.add(session, m);
            }
        }
        return session;
    }


    @Override
    public List<SessionMonster> getMonstruos() {
        //arranca la partida y recupera todos los SessionMonster
        GameSession session = iniciarPartida();
        List<SessionMonster> lista = repositorioSessionMonster.findBySession(session);

        //carga los monster uno por uno de la bdd
        for (SessionMonster sm : lista) {
            Monster m = repositorioMonster.findById(sm.getMonsterId());
            sm.setMonster(m);
        }

        return lista;
    }

    @Override
    public Jugador getJugador() {
        return iniciarPartida().getJugador();
    }

    private List<SessionMonster> getMonstruos(GameSession session) {
        return repositorioSessionMonster.findBySession(session);
    }

    @Override
    public String atacar(int orden) {
        GameSession session = iniciarPartida();
        Jugador jugador = session.getJugador();

        SessionMonster objetivo = getMonstruos().stream()
                .filter(sm -> sm.getOrden() == orden)
                .findFirst()
                .orElse(null);
        if (objetivo == null) {
            return "Monstruo no encontrado.";
        }

        objetivo.setVidaActual(objetivo.getVidaActual() - jugador.getAtk());
        repositorioSessionMonster.update(objetivo);

        // daño aleatorio segun nivel de dificultad
        for (SessionMonster sm : getMonstruos()) {
            if (sm.getVidaActual() > 0) {
                int minDamage = 2, maxDamage = 5;
                int dano = ThreadLocalRandom.current()
                        .nextInt(minDamage, maxDamage + 1);
                //si defiende se reduce a la mitad el daño
                if (jugador.isDefensa()) {
                    dano = dano / 2;
                    jugador.setDefensa(false);
                }

                jugador.setVida(jugador.getVida() - dano);
            }
        }
        repositorioJugador.save(jugador);

        return "Has atacado y tu vida ahora es " + jugador.getVida() + ".";
    }

    @Override
    public String defender() {
        GameSession session = iniciarPartida();
        Jugador jugador = session.getJugador();

        jugador.setDefensa(true);
        repositorioJugador.save(jugador);

        for (SessionMonster sm : getMonstruos(session)) {
            if (sm.getVidaActual() > 0) {
                int minDamage = 1, maxDamage = 3;
                int dano = ThreadLocalRandom.current()
                        .nextInt(minDamage, maxDamage + 1);
                dano = dano / 2;

                jugador.setVida(jugador.getVida() - dano);
            }
        }
        repositorioJugador.save(jugador);

        return "Defiendes este turno. Vida restante: " + jugador.getVida() + ".";
    }

    @Override
    public String usarPocion() {
        GameSession session = iniciarPartida();
        Jugador jugador = session.getJugador();

        int nuevaVida = jugador.getVida() + 30;
        jugador.setVida(nuevaVida);
        repositorioJugador.save(jugador);

        return "Usaste poción. Vida actual: " + jugador.getVida() + ".";
    }

    @Override
    public GameSession getSession() {
        return iniciarPartida();
    }

    @Override
    public void endSession(GameSession session) {
        repositorioSession.delete(session);
    }


}
