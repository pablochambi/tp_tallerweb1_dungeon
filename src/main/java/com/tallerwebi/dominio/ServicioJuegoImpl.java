package com.tallerwebi.dominio;

import java.util.List;

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

    /**
     * Recupera la sesión activa; si no existe, crea una nueva Y SEMBRA los monstruos
     */
    public GameSession iniciarPartida() {
        GameSession session = repositorioSession.findActive();
        if (session == null) {
            // 1) Cargar (o crear) al Jugador con ID=1
            Jugador j = repositorioJugador.findById(1L);
            if (j == null) {
                j = new Jugador();
                j.setNombre("Héroe");
                j.setVida(100);
                j.setAtk(10);
                j.setDefensa(false);
                j.setOro(1000);
                repositorioJugador.save(j);
            }

            // 2) Crear la GameSession y asociar el Jugador persistido
            session = new GameSession();
            session.setJugador(j);
            repositorioSession.save(session);

            // 3) Sembrar los monstruos de la mazmorra
            List<Monster> monsters = repositorioMonster.obtenerTodosLosMonstruos();
            for (Monster m : monsters) {
                repositorioSessionMonster.add(session, m);
            }
        }
        return session;
    }

    @Override
    public List<SessionMonster> getMonstruos() {
        return repositorioSessionMonster.findBySession(iniciarPartida());

    }

    @Override
    public Jugador getJugador() {
        return iniciarPartida().getJugador();
    }

    /**
     * Carga todos los SessionMonster para una sesión dada.
     */
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

        // Jugador ataca
        objetivo.setVidaActual(objetivo.getVidaActual() - jugador.getAtk());
        repositorioSessionMonster.update(objetivo);

        // Monstruos contraatacan
        for (SessionMonster sm : getMonstruos()) {
            if (sm.getVidaActual() > 0) {
                int dano = sm.getMonster().getAtk();
                if (jugador.isDefensa()) {
                    dano /= 2;
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

        // Activa defensa y ataca la mitad
        jugador.setDefensa(true);
        repositorioJugador.save(jugador);

        for (SessionMonster sm : getMonstruos(session)) {
            if (sm.getVidaActual() > 0) {
                int dano = sm.getMonster().getAtk() / 2;
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

        // Aplica poción de vida (+30, por ejemplo)
        int nuevaVida = jugador.getVida() + 30;
        jugador.setVida(nuevaVida);
        repositorioJugador.save(jugador);

        return "Usaste poción. Vida actual: " + jugador.getVida() + ".";
    }
}
