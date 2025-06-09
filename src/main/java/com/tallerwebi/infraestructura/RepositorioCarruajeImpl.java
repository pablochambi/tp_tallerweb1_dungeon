package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.entidades.Carruaje;
import com.tallerwebi.dominio.interfaces.RepositorioCarruaje;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public class RepositorioCarruajeImpl  implements RepositorioCarruaje {


    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioCarruajeImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Carruaje buscarCarruajePorId(Long id) {
        return sessionFactory.getCurrentSession().get(Carruaje.class, id);
    }

    @Override
    public Carruaje guardar(Carruaje carruaje) {
        sessionFactory.getCurrentSession().save(carruaje);
        return carruaje;
    }

    //SELECT * FROM carruaje
    //WHERE usuario_id = [usuarioExistente.id];
    @Override
    public Carruaje buscarCarruajeAsignadoAUnUsuario(Usuario usuarioExistente) {

        Session session = sessionFactory.getCurrentSession();

        return (Carruaje) session.createCriteria(Carruaje.class)
                .add(Restrictions.eq("usuario", usuarioExistente))
                .uniqueResult();

    }

    @Override
    public Carruaje asignarUsuarioAUnCarruje(Carruaje carruajeExistente, Usuario usuarioExistente) {
        Session session = sessionFactory.getCurrentSession();

        // Obtener el carruaje que cumple las condiciones
        //SELECT * FROM carruaje
        //WHERE id = [carruajeExistente.getId()]
        //AND usuario_id IS NULL;

        Carruaje carruajeBus = (Carruaje) session.createCriteria(Carruaje.class)
                .add(Restrictions.eq("id", carruajeExistente.getId()))
                .add(Restrictions.isNull("usuario"))
                .uniqueResult();

        //Actualizar carruaje
        //UPDATE carruaje
        //SET usuario_id = [usuarioExistente.getId()]
        //WHERE id = [carruajeBus.getId()];
        if (carruajeBus != null) {
            carruajeBus.setUsuario(usuarioExistente);
            session.update(carruajeBus);
        }

        return carruajeBus;
    }
}
