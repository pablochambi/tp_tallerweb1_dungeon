package com.tallerwebi.infraestructura;


import com.tallerwebi.dominio.entidades.Item;
import com.tallerwebi.dominio.interfaces.RepositorioItem;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class RepositorioItemImpl implements RepositorioItem {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Item> obtenerTodosLosItems() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Item", Item.class)
                .list();
    }

    @Override
    public Item buscarPorId(Long id) {
        return sessionFactory.getCurrentSession()
                .get(Item.class, id);
    }

    @Override
    public List<Item> listarTodos() {
        return sessionFactory
                .getCurrentSession()
                .createQuery("FROM Item", Item.class)
                .list();
    }
}