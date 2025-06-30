package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidades.*;
import com.tallerwebi.dominio.interfaces.*;
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

        // 1) localiza héroe y monstruo
        SessionHero sh = getHeroesDeSesion(u).stream()
                .filter(h -> h.getOrden() == heroOrden)
                .findFirst().orElse(null);
        SessionMonster sm = getMonstruos(u).stream()
                .filter(m -> m.getOrden() == monsterOrden)
                .findFirst().orElse(null);

        if (sh == null) return "Héroe no encontrado.";
        if (sm == null) return "Monstruo no encontrado.";

        // 2) héroe golpea
        sm.takeDamage(sh.damageOutput());
        smRepo.update(sm);

        // 3) monstruos vivos contraatacan
        List<SessionMonster> vivos = getMonstruos(u).stream()
                .filter(m -> m.getVidaActual() > 0)
                .collect(Collectors.toList());
        for (SessionMonster m : vivos) {
            sh.takeDamage(m.getMonster().getAtk());
            shRepo.update(sh);
        }

        // 4) arma el mensaje final
        String atacantes = vivos.isEmpty()
                ? "Nadie"
                : vivos.stream()
                .map(x -> x.getMonster().getNombre())
                .collect(Collectors.joining(", "));
        return String.format(
                "Has atacado. Contraatacan: %s. Tu héroe %s ahora tiene %d/%d HP.",
                atacantes,
                sh.getHero().getNombre(),
                sh.getVidaActual(),
                sh.getHero().getMaxVida()
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
    public Expedition getExpedicionActiva(Usuario u) {
        GameSession s = iniciarPartida(u);
        return expeditionRepo
                .findBySessionAndCompletedFalse(s)
                .orElseThrow(() -> new IllegalStateException("No hay expedición activa"));
    }

    @Override
    public void endSession(GameSession session) {
        if (session != null) {
            smRepo.deleteBySession(session);
            shRepo.deleteBySession(session);
            sessionRepo.delete(session);
        }
    }

    @Override
    public void terminarExpedicion(Usuario u) {
        GameSession session = iniciarPartida(u);

        // 1) completa la expedición activa
        Expedition exp = expeditionRepo
                .findBySessionAndCompletedFalse(session)
                .orElseThrow(() -> new IllegalStateException("No hay expedición activa"));
        int oldNumber = exp.getNumber();
        exp.setCompleted(true);
        expeditionRepo.save(exp);

        // 2) recompensa de 250 de oro
        u.setOro(u.getOro() + 250);
        usuarioRepo.modificar(u);

        // 3) limpia monstruos y héroes de sesión
        smRepo.deleteBySession(session);
        shRepo.deleteBySession(session);

        // 4) crea la siguiente expedición N+1
        Expedition next = crearExpedicion(session, oldNumber + 1);

        // 5) siembra héroes (los que el usuario tiene en su carruaje)
        seedHeroes(session);

        // 6) siembra los monstruos buffeados de la nueva expedición
        seedMonstersBuffed(session, next.getNumber());
    }


    // Metodos auxiliares

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

    private void seedMonsters(GameSession session, int expeditionNumber) {
        // 1) calculamos el factor de buff: 1.0 + 0.1 por cada expedición completada anterior
        double factor = 1.0 + (expeditionNumber - 1) * 0.10;

        // 2) traemos y barajamos todos los monstruos
        List<Monster> todos = new ArrayList<>(monsterRepo.obtenerTodosLosMonstruos());
        Collections.shuffle(todos);

        // 3) limitamos a 3 y los insertamos
        int orden = 1;
        for (Monster m : todos.subList(0, 3)) {
            SessionMonster sm = new SessionMonster();
            sm.setSession(session);
            sm.setMonster(m);
            sm.setExpeditionNumber(expeditionNumber);
            sm.setDungeonNumber(1);
            sm.setOrden(orden++);

            // 4) Vida buffeada
            int vidaBuffeada = (int) Math.round(m.getVida() * factor);
            sm.setVidaActual(vidaBuffeada);

            sessionRepo.getSessionFactory()
                    .getCurrentSession()
                    .save(sm);
        }
    }


    private void seedHeroes(GameSession session) {
        List<Heroe> heroes = servicioRecluta.getHeroesEnCarruaje(session.getUsuario().getId());
        int orden = 1;
        for (Heroe h : heroes) {
            shRepo.add(session, h, orden++);
        }
    }

    private void seedMonstersBuffed(GameSession session, int expeditionNumber) {
        // factor de buff: +10% por cada expedición pasada
        double factor = 1.0 + (expeditionNumber - 1) * 0.10;

        // baraja y toma 3
        List<Monster> todos = new ArrayList<>(monsterRepo.obtenerTodosLosMonstruos());
        Collections.shuffle(todos);

        int orden = 1;
        for (Monster base : todos.subList(0, 3)) {
            // clonar plantilla para no alterar el original
            Monster buffed = new Monster();
            buffed.setId     (base.getId());
            buffed.setNombre (base.getNombre());
            buffed.setImagen (base.getImagen());
            // aplicar buff
            buffed.setVida((int) Math.round(base.getVida() * factor));
            buffed.setAtk ((int) Math.round(base.getAtk()  * factor));
            // guarda en la sesión, el repo calculará el orden interno si usas la firma
            // void add(GameSession, Monster, int dungeonNumber)
            smRepo.add(session, buffed, expeditionNumber);
            orden++;
        }
    }

    @Override
    public boolean tieneSesionActiva(Usuario u) {
        return sessionRepo.findActive(u) != null;
    }

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
