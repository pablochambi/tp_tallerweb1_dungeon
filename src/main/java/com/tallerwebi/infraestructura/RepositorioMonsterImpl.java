package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Monster;
import com.tallerwebi.dominio.interfaces.RepositorioMonster;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class RepositorioMonsterImpl implements RepositorioMonster {

    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioMonsterImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session session() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Monster> obtenerTodosLosMonstruos() {
        return session()
                .createCriteria(Monster.class)
                .list();
    }

    @Override
    public Monster findById(Long monsterId) {
        return session().get(Monster.class, monsterId);
    }
}
