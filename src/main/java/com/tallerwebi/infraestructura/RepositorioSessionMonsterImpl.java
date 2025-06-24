package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.GameSession;
import com.tallerwebi.dominio.entidades.Monster;
import com.tallerwebi.dominio.entidades.SessionMonster;
import com.tallerwebi.dominio.interfaces.RepositorioMonster;
import com.tallerwebi.dominio.interfaces.RepositorioSessionMonster;
import com.tallerwebi.dominio.interfaces.RepositorioSessionMonsterJPA;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class RepositorioSessionMonsterImpl
        implements RepositorioSessionMonster {

    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioSessionMonsterImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session session() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void add(GameSession session, Monster monster) {
        // calculo de orden con Criteria y Projections
        Criteria crit = session()
                .createCriteria(SessionMonster.class)
                .add(Restrictions.eq("session", session))
                .setProjection(Projections.rowCount());
        Long count = (Long) crit.uniqueResult();

        SessionMonster sm = new SessionMonster();
        sm.setSession(session);
        sm.setMonster(monster);
        sm.setVidaActual(monster.getVida());
        sm.setExpeditionNumber(1);
        sm.setDungeonNumber(1);
        sm.setOrden(count.intValue() + 1);
        session().save(sm);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SessionMonster> findBySession(GameSession session) {
        return session()
                .createCriteria(SessionMonster.class)
                .add(Restrictions.eq("session", session))
                .addOrder(Order.asc("orden"))
                .list();
    }

    @Override
    public void update(SessionMonster sm) {
        session().update(sm);
    }

    @Override
    public void deleteBySession(GameSession session) {
        session()
                .createQuery("DELETE FROM SessionMonster sm WHERE sm.session = :s")
                .setParameter("s", session)
                .executeUpdate();
    }
}

