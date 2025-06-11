package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Jugador;
import com.tallerwebi.dominio.interfaces.RepositorioJugador;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class RepositorioJugadorImpl implements RepositorioJugador {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Jugador findById(Long id) {
        return sessionFactory.getCurrentSession().get(Jugador.class, id);
    }

    @Override
    public void save(Jugador j) {
        sessionFactory.getCurrentSession().saveOrUpdate(j);
    }
}
