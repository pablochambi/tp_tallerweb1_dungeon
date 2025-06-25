package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidades.GameSession;
import com.tallerwebi.dominio.entidades.Monster;
import com.tallerwebi.dominio.entidades.SessionHero;
import com.tallerwebi.dominio.entidades.SessionMonster;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.interfaces.RepositorioHeroSession;
import com.tallerwebi.dominio.interfaces.RepositorioMonster;
import com.tallerwebi.dominio.interfaces.RepositorioSession;
import com.tallerwebi.dominio.interfaces.RepositorioSessionMonster;
import com.tallerwebi.dominio.interfaces.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ServicioJuegoImpl implements ServicioJuego {

    private final RepositorioSession sessionRepo;
    private final RepositorioSessionMonster smRepo;
    private final RepositorioHeroSession shRepo;
    private final RepositorioMonster monsterRepo;
    private final RepositorioUsuario usuarioRepo;

    @Autowired
    public ServicioJuegoImpl(
            RepositorioSession sessionRepo,
            RepositorioSessionMonster smRepo,
            RepositorioHeroSession shRepo,
            RepositorioMonster monsterRepo,
            RepositorioUsuario usuarioRepo
    ) {
        this.sessionRepo = sessionRepo;
        this.smRepo      = smRepo;
        this.shRepo      = shRepo;
        this.monsterRepo = monsterRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @Override
    public GameSession iniciarPartida() {
        // 1) Obtener usuario (por ejemplo ID fijo 1 o contexto de seguridad)
        Usuario u = usuarioRepo.buscarUsuarioPorId(1L);
        if (u == null) throw new IllegalStateException("Usuario 1 no encontrado");

        // 2) Buscar o crear sesión para este usuario
        GameSession session = sessionRepo.findActive(u);
        if (session == null) {
            session = sessionRepo.startNew(u);
            // Seed inicial de monsters y heroes omitido aquí
        }
        return session;
    }

    @Override
    public GameSession crearNuevaMazmorra() {
        // delegar a startNew también crea sesión si no existe
        GameSession session = iniciarPartida();
        // Limpiar estado previo
        smRepo.deleteBySession(session);
        shRepo.deleteBySession(session);
        // Sembrar monsters
        List<Monster> all = new ArrayList<>(monsterRepo.obtenerTodosLosMonstruos());
        Collections.shuffle(all);
        all.stream().limit(3).forEach(m -> smRepo.add(session, m));
        return session;
    }

    @Override
    public void reiniciarMazmorra() {
        GameSession session = iniciarPartida();
        smRepo.deleteBySession(session);
        shRepo.deleteBySession(session);
        List<Monster> all = new ArrayList<>(monsterRepo.obtenerTodosLosMonstruos());
        Collections.shuffle(all);
        all.stream().limit(3).forEach(m -> smRepo.add(session, m));
    }

    @Override
    public GameSession getSession() {
        return iniciarPartida();
    }

    @Override
    public Usuario getUsuario() {
        return iniciarPartida().getUsuario();
    }

    @Override
    public List<SessionMonster> getMonstruos() {
        return smRepo.findBySession(iniciarPartida());
    }

    @Override
    public List<SessionHero> getHeroesDeSesion() {
        return shRepo.findBySession(iniciarPartida());
    }

    @Override
    public String atacar(int heroOrden, int monsterOrden) {
        GameSession session = iniciarPartida();
        SessionHero sh = getHeroesDeSesion().stream()
                .filter(h -> h.getOrden() == heroOrden).findFirst().orElse(null);
        SessionMonster sm = getMonstruos().stream()
                .filter(m -> m.getOrden() == monsterOrden).findFirst().orElse(null);
        if (sh == null || sm == null) return "Entidad no encontrada";
        sm.takeDamage(sh.damageOutput());
        smRepo.update(sm);
        getMonstruos().stream()
                .filter(m -> m.getVidaActual() > 0)
                .forEach(m -> { sh.takeDamage(m.getMonster().getAtk()); shRepo.update(sh); });
        return "Turno procesado";
    }

    @Override
    public String defender(int heroOrden) {
        GameSession session = iniciarPartida();
        SessionHero sh = getHeroesDeSesion().stream()
                .filter(h -> h.getOrden() == heroOrden).findFirst().orElse(null);
        if (sh == null) return "Héroe no encontrado.";
        sh.defend(); shRepo.update(sh);
        return "Defensa activa.";
    }

    @Override
    public String usarPocion(int heroOrden) {
        GameSession session = iniciarPartida();
        SessionHero sh = getHeroesDeSesion().stream()
                .filter(h -> h.getOrden() == heroOrden).findFirst().orElse(null);
        if (sh == null) return "Héroe no encontrado.";
        sh.heal(30); shRepo.update(sh);
        return "Poción usada.";
    }

    @Override
    public void endSession(GameSession current) {
        if (current != null) {
            smRepo.deleteBySession(current);
            shRepo.deleteBySession(current);
            sessionRepo.delete(current);
        }
    }
}
