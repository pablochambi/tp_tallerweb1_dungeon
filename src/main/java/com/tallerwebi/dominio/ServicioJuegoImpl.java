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


        GameSession session = sessionRepo.findActive(u);

        if (session == null) {
            session = sessionRepo.startNew(u);

            smRepo.deleteBySession(session);
            shRepo.deleteBySession(session);

            //  seed de monstruos
            List<Monster> allMon = new ArrayList<>(monsterRepo.obtenerTodosLosMonstruos());
            Collections.shuffle(allMon);
            GameSession finalSession = session;
            allMon.stream().limit(3)
                    .forEach(m -> smRepo.add(finalSession, m /*, dungeonNumber si tienes multi‐nivel*/));

            // 2seed de héroes (los que haya seleccionado el usuario)
            // asumimos que tienes un servicio que te da “los id” de los héroes del carruaje para este usuario
            List<Heroe> heroes = servicioRecluta.getHeroesEnCarruaje(u.getId());
            int orden = 1;
            for (Heroe h : heroes) {
                shRepo.add(session, h, orden++);
            }
        }

        return session;
    }

    @Override
    public GameSession crearNuevaMazmorra(Usuario u) {
        // Limpia y genera primera mazmorra de la sesión
        GameSession session = iniciarPartida(u);
        smRepo.deleteBySession(session);
        shRepo.deleteBySession(session);

        List<Monster> all = new ArrayList<>(monsterRepo.obtenerTodosLosMonstruos());
        Collections.shuffle(all);
        all.stream()
                .limit(3)
                .forEach(m -> smRepo.add(session, m, /* dungeonNumber = */ 1));
        return session;
    }

    @Override
    public void reiniciarMazmorra(Usuario u) {
        GameSession session = iniciarPartida(u);
        smRepo.deleteBySession(session);
        shRepo.deleteBySession(session);

        List<Monster> all = new ArrayList<>(monsterRepo.obtenerTodosLosMonstruos());
        Collections.shuffle(all);
        all.stream()
                .limit(3)
                .forEach(m -> smRepo.add(session, m, /* dungeonNumber = */ 1));
    }

    @Override
    public GameSession getSession(Usuario u) {
        return iniciarPartida(u);
    }

    @Override
    public Usuario getUsuario(Usuario u) {
        return iniciarPartida(u).getUsuario();
    }

    @Override
    public List<SessionMonster> getMonstruos(Usuario u) {
        GameSession session = iniciarPartida(u);
        return smRepo.findBySession(session);
    }

    @Override
    public List<SessionHero> getHeroesDeSesion(Usuario u) {
        GameSession session = iniciarPartida(u);
        return shRepo.findBySession(session);
    }

    @Override
    public String atacar(Usuario u, int heroOrden, int monsterOrden) {
        GameSession session = iniciarPartida(u);
        // 1) Heroe golpea
        SessionHero sh = getHeroesDeSesion(u).stream()
                .filter(h -> h.getOrden() == heroOrden)
                .findFirst().orElse(null);
        SessionMonster sm = getMonstruos(u).stream()
                .filter(m -> m.getOrden() == monsterOrden)
                .findFirst().orElse(null);
        if (sh == null)    return "Héroe no encontrado.";
        if (sm == null)    return "Monstruo no encontrado.";

        sm.takeDamage(sh.damageOutput());
        smRepo.update(sm);

        // 2) Monstruos vivos contraatacan
        List<SessionMonster> vivos = getMonstruos(u).stream()
                .filter(m -> m.getVidaActual() > 0)
                .toList();
        for (SessionMonster m : vivos) {
            sh.takeDamage(m.getMonster().getAtk());
            shRepo.update(sh);
        }

        // 3) Construir mensaje
        String atacantes = vivos.isEmpty()
                ? "Nadie"
                : vivos.stream()
                .map(x -> x.getMonster().getNombre())
                .reduce((a,b) -> a + ", " + b).orElse("");
        return String.format(
                "Has atacado. %s. Tu héroe %s ahora tiene %d/%d HP.",
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
        return "Tu héroe " + sh.getHero().getNombre() + " se defiende este turno.";
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
        return "Tu héroe " + sh.getHero().getNombre() +
                " recupera vida: ahora " + sh.getVidaActual() +
                "/" + sh.getHero().getMaxVida() + ".";
    }

    @Override
    public void siguienteMazmorra(Usuario u) {
        GameSession session = iniciarPartida(u);

        // Encuentra la expedición activa o lanza error
        Expedition exp = expeditionRepo
                .findBySessionAndCompletedFalse(session)
                .orElseThrow(() ->
                        new IllegalStateException("No hay expedición activa para avanzar"));

        int nextDungeon = exp.getNumber() + 1;
        if (nextDungeon > 3) {
            // Se completó la expedición
            exp.setCompleted(true);
            expeditionRepo.save(exp);
            endSession(session);
        } else {
            // Avanzar a la siguiente mazmorra
            exp.setNumber(nextDungeon);
            expeditionRepo.save(exp);

            // Borrar monstruos de la mazmorra anterior
            smRepo.deleteBySessionAndDungeonNumber(session, nextDungeon - 1);
            // Sembrar nueva oleada
            seedMonsters(session, nextDungeon);
        }
    }

    @Override
    public void endSession(GameSession current) {
        if (current != null) {
            smRepo.deleteBySession(current);
            shRepo.deleteBySession(current);
            sessionRepo.delete(current);
        }
    }

    @Override
    public GameSession getSession() {
        Usuario u = usuarioRepo.buscarUsuarioPorId(1L);
        if (u == null) throw new IllegalStateException("Usuario 1 no encontrado");
        return iniciarPartida(u);
    }

    @Override
    public Usuario getUsuario() {
        return getSession().getUsuario();
    }

    private void seedMonsters(GameSession session, int dungeonNumber) {
        List<Monster> all = new ArrayList<>(monsterRepo.obtenerTodosLosMonstruos());
        Collections.shuffle(all);
        all.stream()
                .limit(3)
                .forEach(m -> smRepo.add(session, m, dungeonNumber));
    }
}
