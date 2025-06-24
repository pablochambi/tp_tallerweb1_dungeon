package com.tallerwebi.infraestructura;


import com.tallerwebi.dominio.entidades.Inventario;
import com.tallerwebi.dominio.entidades.Item;
import com.tallerwebi.dominio.interfaces.RepositorioItem;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
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
    public void guardarItem(Item item) {
        sessionFactory.getCurrentSession().save(item);
    }

    @Override
    public List<Item> obtenerLosItemsByInventario(Long idInventario) {
        return sessionFactory.getCurrentSession().createCriteria(Item.class)
                .createAlias("inventario", "inv")
                .add(Restrictions.eq("inventario.id", idInventario))
                .list();
    }

}