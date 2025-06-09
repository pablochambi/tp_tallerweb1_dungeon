package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Monster;
import com.tallerwebi.dominio.RepositorioMonster;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class RepositorioMonsterImpl implements RepositorioMonster {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Monster> obtenerTodosLosMonstruos() {
        // Esto carga TODOS los monstruos de la tabla Monster
        return sessionFactory
                .getCurrentSession()
                .createQuery("FROM Monster", Monster.class)
                .list();
    }
}