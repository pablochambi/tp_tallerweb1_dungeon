package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidades.*;
import com.tallerwebi.dominio.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class ServicioJuegoImpl implements ServicioJuego {

    private final RepositorioSession sessionRepo;
    private final RepositorioSessionMonster smRepo;
    private final RepositorioHeroSession shRepo;
    private final RepositorioMonster monsterRepo;
    private final RepositorioUsuario usuarioRepo;
    private final RepositorioExpedition expeditionRepo;
    private final ServicioRecluta servicioRecluta;

    @Autowired
    public ServicioJuegoImpl(
            RepositorioSession sessionRepo,
            RepositorioSessionMonster smRepo,
            RepositorioHeroSession shRepo,
            RepositorioMonster monsterRepo,
            RepositorioUsuario usuarioRepo,
            RepositorioExpedition expeditionRepo,
            ServicioRecluta servicioRecluta
    ) {
        this.sessionRepo     = sessionRepo;
        this.smRepo          = smRepo;
        this.shRepo          = shRepo;
        this.monsterRepo     = monsterRepo;
        this.usuarioRepo     = usuarioRepo;
        this.expeditionRepo  = expeditionRepo;
        this.servicioRecluta = servicioRecluta;
    }

    @Override
    public GameSession iniciarPartida(Usuario u) {
        if (u == null) throw new IllegalArgumentException("Usuario nulo");

        // obtengo o creo sesion
        GameSession session = sessionRepo.findActive(u);
        boolean isNew = false;
        if (session == null) {
            session = sessionRepo.startNew(u);
            isNew = true;
        }

        // obtengo o creo la expedicion activa (mazmorra 1 2 o 3)
        GameSession finalSession = session;
        Expedition exp = expeditionRepo
                .findBySessionAndCompletedFalse(session)
                .orElseGet(() -> crearExpedicion(finalSession, 1));


        if (isNew) {
            seedExpedition(session, exp.getNumber());
        }

        else {
            boolean tieneMonstruos = !smRepo
                    .findBySessionAndExpeditionNumber(session, exp.getNumber())
                    .isEmpty();
            if (!tieneMonstruos) {
                seedMonsters(session, exp.getNumber());
            }
        }

        return session;
    }

    @Override
    public void siguienteMazmorra(Usuario u) {
        GameSession session = iniciarPartida(u);

        Expedition exp = expeditionRepo
                .findBySessionAndCompletedFalse(session)
                .orElseThrow(() ->
                        new IllegalStateException("No hay expedición activa para avanzar"));

        int next = exp.getNumber() + 1;
        if (next > 3) {
            // completa y limpia
            exp.setCompleted(true);
            expeditionRepo.save(exp);
            endSession(session);
        } else {
            // avanza mazmorra
            exp.setNumber(next);
            expeditionRepo.save(exp);

            // borra monstruos de la mazmorra anterior y siembra la nueva
            smRepo.deleteBySessionAndExpeditionNumber(session, next - 1);
            seedMonsters(session, next);
        }
    }

    @Override
    public GameSession crearNuevaMazmorra(Usuario u) {
        GameSession session = iniciarPartida(u);

        // limpia heroes y monster
        smRepo.deleteBySession(session);
        shRepo.deleteBySession(session);

        // siembra solo monstruos de la mazmorra 1
        seedMonsters(session, 1);

        return session;
    }

    @Override
    public void reiniciarMazmorra(Usuario u) {
        GameSession session = iniciarPartida(u);
        smRepo.deleteBySession(session);
        shRepo.deleteBySession(session);
        seedMonsters(session, 1);
    }

    @Override
    public List<SessionMonster> getMonstruos(Usuario u) {
        GameSession session = iniciarPartida(u);
        Expedition exp = expeditionRepo
                .findBySessionAndCompletedFalse(session)
                .orElseThrow(() -> new IllegalStateException("No hay expedición activa"));
        return smRepo.findBySessionAndExpeditionNumber(session, exp.getNumber());
    }

    @Override
    public List<SessionHero> getHeroesDeSesion(Usuario u) {
        GameSession session = iniciarPartida(u);
        return shRepo.findBySession(session);
    }

    @Override
    public String atacar(Usuario u, int heroOrden, int monsterOrden) {
        GameSession session = iniciarPartida(u);
        // héroe ataca
        SessionHero sh = getHeroesDeSesion(u).stream()
                .filter(h -> h.getOrden() == heroOrden)
                .findFirst().orElse(null);
        SessionMonster sm = getMonstruos(u).stream()
                .filter(m -> m.getOrden() == monsterOrden)
                .findFirst().orElse(null);

        if (sh == null) return "Héroe no encontrado.";
        if (sm == null) return "Monstruo no encontrado.";

        sm.takeDamage(sh.damageOutput());
        smRepo.update(sm);

        // monstruos vivos contraatacan
        List<SessionMonster> vivos = getMonstruos(u).stream()
                .filter(m -> m.getVidaActual() > 0)
                .toList();
        for (SessionMonster m : vivos) {
            sh.takeDamage(m.getMonster().getAtk());
            shRepo.update(sh);
        }

        String atacantes = vivos.isEmpty()
                ? "Nadie"
                : vivos.stream()
                .map(x -> x.getMonster().getNombre())
                .reduce((a, b) -> a + ", " + b).orElse("");
        return String.format(
                "Has atacado. Contraatacan: %s. Tu héroe %s ahora tiene %d/%d HP.",
                atacantes, sh.getHero().getNombre(),
                sh.getVidaActual(), sh.getHero().getMaxVida()
        );
    }

    @Override
    public String defender(Usuario u, int heroOrden) {
        GameSession session = iniciarPartida(u);
        SessionHero sh = getHeroesDeSesion(u).stream()
                .filter(h -> h.getOrden() == heroOrden)
                .findFirst().orElse(null);
        if (sh == null) return "Héroe no encontrado.";
        sh.defend();
        shRepo.update(sh);
        return "Tu héroe " + sh.getHero().getNombre() + " se defiende.";
    }

    @Override
    public String usarPocion(Usuario u, int heroOrden) {
        GameSession session = iniciarPartida(u);
        SessionHero sh = getHeroesDeSesion(u).stream()
                .filter(h -> h.getOrden() == heroOrden)
                .findFirst().orElse(null);
        if (sh == null) return "Héroe no encontrado.";
        sh.heal(30);
        shRepo.update(sh);
        return String.format(
                "Tu héroe %s recupera vida: ahora %d/%d.",
                sh.getHero().getNombre(),
                sh.getVidaActual(),
                sh.getHero().getMaxVida()
        );
    }

    @Override
    public void endSession(GameSession session) {
        if (session != null) {
            smRepo.deleteBySession(session);
            shRepo.deleteBySession(session);
            sessionRepo.delete(session);
        }
    }

    // ========================================================================
    // Métodos auxiliares de seed
    // ========================================================================
    private Expedition crearExpedicion(GameSession session, int numero) {
        Expedition e = new Expedition();
        e.setSession(session);
        e.setNumber(numero);
        e.setCompleted(false);
        return expeditionRepo.save(e);
    }

    private void seedExpedition(GameSession session, int dungeonNumber) {
        seedMonsters(session, dungeonNumber);
        seedHeroes(session);
    }

    private void seedMonsters(GameSession session, int dungeonNumber) {
        List<Monster> todos = new ArrayList<>(monsterRepo.obtenerTodosLosMonstruos());
        Collections.shuffle(todos);
        todos.stream()
                .limit(3)
                .forEach(m -> smRepo.add(session, m, dungeonNumber));
    }

    private void seedHeroes(GameSession session) {
        List<Heroe> heroes = servicioRecluta.getHeroesEnCarruaje(session.getUsuario().getId());
        int orden = 1;
        for (Heroe h : heroes) {
            shRepo.add(session, h, orden++);
        }
    }

    // ========================================================================
    // Sobrecargas sin parámetros (para compilar; no las usas desde tu controlador)
    // ========================================================================
    @Override
    public GameSession getSession() {
        throw new UnsupportedOperationException("Use getSession(Usuario) en su lugar");
    }

    @Override
    public Usuario getUsuario() {
        throw new UnsupportedOperationException("Use getUsuario(Usuario) en su lugar");
    }

    @Override
    public GameSession getSession(Usuario u) {
        return iniciarPartida(u);
    }

    @Override
    public Usuario getUsuario(Usuario u) {
        return iniciarPartida(u).getUsuario();
    }
}
