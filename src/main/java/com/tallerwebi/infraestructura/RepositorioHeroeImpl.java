package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Heroe;
import com.tallerwebi.dominio.interfaces.RepositorioHeroe;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public class RepositorioHeroeImpl implements RepositorioHeroe {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioHeroeImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }


    @Override
    public Heroe buscarHeroePorId(Long id) {
        return sessionFactory.getCurrentSession().get(Heroe.class, id);
    }

    @Override
    public Heroe guardar(Heroe heroe) {
        sessionFactory.getCurrentSession().save(heroe);
        return heroe;
    }
}
