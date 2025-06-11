package com.tallerwebi.dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.tallerwebi.dominio.entidades.GameSession;
import com.tallerwebi.dominio.entidades.Jugador;
import com.tallerwebi.dominio.entidades.Monster;
import com.tallerwebi.dominio.interfaces.RepositorioJugador;
import com.tallerwebi.dominio.interfaces.RepositorioMonster;
import com.tallerwebi.dominio.interfaces.RepositorioSession;
import com.tallerwebi.dominio.interfaces.RepositorioSessionMonster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServicioJuegoImpl implements ServicioJuego {

    @Autowired private RepositorioJugador jugadorRepo;
    @Autowired private RepositorioSession sessionRepo;
    @Autowired private RepositorioMonster monsterRepo;
    @Autowired private RepositorioSessionMonster smRepo;

   //recupera sesion activa, sino crea una nueva
   @Override
   public GameSession iniciarPartida() {
       GameSession session = sessionRepo.findActive();
       if (session == null) {
           session = crearNuevaMazmorra();
       }
       return session;
   }
    @Override
    public GameSession crearNuevaMazmorra() {
        // jugador fijo con id 1 (se crea si no existe)
        Jugador j = jugadorRepo.findById(1L);
        if (j == null) {
            j = new Jugador();
            j.setNombre("Héroe");
            j.setVida(100);
            j.setAtk(10);
            j.setDefensa(false);
            j.setOro(1000);
            jugadorRepo.save(j);
        }

        GameSession session = new GameSession();
        session.setJugador(j);
        sessionRepo.save(session);

        // agregar 3 monstruos random
        List<Monster> all = monsterRepo.obtenerTodosLosMonstruos();
        Collections.shuffle(all);
        int toSeed = Math.min(3, all.size());
        for (int i = 0; i < toSeed; i++) {
            smRepo.add(session, all.get(i));
        }
        return session;
    }

    @Override
    public void reiniciarMazmorra() {
        GameSession session = sessionRepo.findActive();
        if (session == null) {
            // si no existe la creamos
            crearNuevaMazmorra();
        } else {
            // eliminar monstruos viejos
            smRepo.deleteBySession(session);
            // agregar monstruos 3 nuevos
            List<Monster> all = monsterRepo.obtenerTodosLosMonstruos();
            Collections.shuffle(all);
            int toSeed = Math.min(3, all.size());
            for (int i = 0; i < toSeed; i++) {
                smRepo.add(session, all.get(i));
            }
        }
    }


    @Override
    public Jugador getJugador() {
        return iniciarPartida().getJugador();
    }

    @Override
    public List<SessionMonster> getMonstruos() {
        return smRepo.findBySession(iniciarPartida());
    }

    @Override
    public String atacar(int orden) {
        GameSession session = iniciarPartida();
        Jugador jugador = session.getJugador();

        // 1) Atacar al monstruo objetivo
        SessionMonster objetivo = getMonstruos().stream()
                .filter(sm -> sm.getOrden() == orden)
                .findFirst()
                .orElse(null);
        if (objetivo == null) {
            return "Monstruo no encontrado.";
        }
        objetivo.setVidaActual(objetivo.getVidaActual() - jugador.getAtk());
        smRepo.update(objetivo);

        // 2) Monstruos vivos contraatacan y registramos sus nombres
        List<SessionMonster> vivos = getMonstruos().stream()
                .filter(sm -> sm.getVidaActual() > 0)
                .collect(Collectors.toList());

        List<String> atacantes = new ArrayList<>();
        for (SessionMonster sm : vivos) {
            atacantes.add(sm.getMonster().getNombre());
            int dano = sm.getMonster().getAtk();
            if (jugador.isDefensa()) {
                dano /= 2;
                jugador.setDefensa(false);
            }
            jugador.setVida(jugador.getVida() - dano);
        }
        jugadorRepo.save(jugador);

        // 3) Construimos el texto de quién atacó
        String textoAtacantes;
        if (atacantes.isEmpty()) {
            textoAtacantes = "Nadie";
        } else if (atacantes.size() == 1) {
            textoAtacantes = atacantes.get(0) + " te contraataca";
        } else {
            // Unir con comas y "y" antes del último
            String lista = String.join(", ", atacantes.subList(0, atacantes.size()-1))
                    + " y " + atacantes.get(atacantes.size()-1);
            textoAtacantes = lista + " te contraatacan";
        }

        // 4) Devolver mensaje final
        return String.format(
                "Has atacado. %s y ahora tu vida es %d.",
                textoAtacantes,
                jugador.getVida()
        );
    }

    @Override
    public String defender() {
        GameSession session = iniciarPartida();
        Jugador jugador = session.getJugador();

        jugador.setDefensa(true);
        jugadorRepo.save(jugador);

        for (SessionMonster sm : getMonstruos()) {
            if (sm.getVidaActual() > 0) {
                int dano = sm.getMonster().getAtk() / 2;
                jugador.setVida(jugador.getVida() - dano);
            }
        }
        jugadorRepo.save(jugador);
        return "Defiendes este turno. Vida restante: " + jugador.getVida() + ".";
    }

    @Override
    public String usarPocion() {
        GameSession session = iniciarPartida();
        Jugador jugador = session.getJugador();

        jugador.setVida(jugador.getVida() + 30);
        jugadorRepo.save(jugador);
        return "Usaste poción. Vida actual: " + jugador.getVida() + ".";
    }

    @Override
    public GameSession getSession() {
        return iniciarPartida();
    }

    @Override
    public void endSession(GameSession current) {
        if (current != null) {
            sessionRepo.delete(current);
        }
    }
}
