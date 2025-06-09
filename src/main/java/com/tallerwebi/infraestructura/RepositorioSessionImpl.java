package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.GameSession;
import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.RepositorioJugador;
import com.tallerwebi.dominio.RepositorioSession;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class RepositorioSessionImpl implements RepositorioSession {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private RepositorioJugador repositorioJugador;

    @Override
    public GameSession startNew() {
        // 1) Asegurar Jugador existente en BDD
        Jugador jugador = repositorioJugador.findById(1L);
        if (jugador == null) {
            jugador = new Jugador();
            jugador.setNombre("Heroe");
            jugador.setVida(100);
            jugador.setAtk(10);
            jugador.setDefensa(false);
            jugador.setOro(1000);
            repositorioJugador.save(jugador);
        }
        // 2) Crear y guardar la sesión ya ligada a ese jugador
        GameSession session = new GameSession();
        session.setJugador(jugador);
        sessionFactory.getCurrentSession().save(session);
        return session;
    }

    @Override
    public GameSession findActive() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM GameSession g WHERE g.active = true", GameSession.class)
                .uniqueResult();
    }

    @Override
    public void save(GameSession s) {
        sessionFactory.getCurrentSession().saveOrUpdate(s);
    }
}
