package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.GameSession;
import com.tallerwebi.dominio.Monster;
import com.tallerwebi.dominio.SessionMonster;
import com.tallerwebi.dominio.RepositorioSessionMonster;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class RepositorioSessionMonsterImpl implements RepositorioSessionMonster {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void add(GameSession session, Monster monster) {
        SessionMonster sm = new SessionMonster();
        sm.setSessionId(session.getId());
        sm.setMonsterId(monster.getId());
        sm.setVidaActual(monster.getVida());
        // calculo del orden
        Long count = sessionFactory.getCurrentSession()
                .createQuery("SELECT COUNT(sm) FROM SessionMonster sm WHERE sm.sessionId = :sid", Long.class)
                .setParameter("sid", session.getId())
                .uniqueResult();
        sm.setOrden(count.intValue() + 1);
        sessionFactory.getCurrentSession().save(sm);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SessionMonster> findBySession(GameSession session) {
        return sessionFactory.getCurrentSession()
                .createQuery(
                        "FROM SessionMonster sm WHERE sm.sessionId = :sid ORDER BY sm.orden",
                        SessionMonster.class
                )
                .setParameter("sid", session.getId())
                .list();
    }

    @Override
    public void update(SessionMonster sessionMonster) {
        sessionFactory.getCurrentSession().update(sessionMonster);
    }
}
