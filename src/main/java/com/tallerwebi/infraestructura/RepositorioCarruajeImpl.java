package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.entidades.Carruaje;
import com.tallerwebi.dominio.interfaces.RepositorioCarruaje;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public class RepositorioCarruajeImpl  implements RepositorioCarruaje {


    private final RepositorioUsuarioImpl repositorioUsuario;
    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioCarruajeImpl(SessionFactory sessionFactory, RepositorioUsuarioImpl repositorioUsuario) {
        this.sessionFactory = sessionFactory;
        this.repositorioUsuario = repositorioUsuario;
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

        Usuario usuario = repositorioUsuario.buscarUsuarioPorId(usuarioExistente.getId());
        if(usuario == null) throw new RuntimeException("No se encontro usuario en BD");

        Session session = sessionFactory.getCurrentSession();

        Carruaje carruaje = (Carruaje) session.createCriteria(Carruaje.class)
                .add(Restrictions.eq("usuario", usuarioExistente))
                .uniqueResult();

//        if(carruaje == null) throw new RuntimeException("No se encontro carruaje asignado a un usuario en BD");
//
//        if(carruaje.getId() == null) throw new RuntimeException("El id de carruaje es nulo");


        return carruaje;

    }

    @Override
    public Carruaje asignarUsuarioAUnCarruje(Carruaje carruajeExistente, Usuario usuarioExistente) {
        Carruaje c = sessionFactory.getCurrentSession().get(Carruaje.class, carruajeExistente.getId());
        Usuario u = sessionFactory.getCurrentSession().get(Usuario.class, usuarioExistente.getId());
        if(u == null) throw new RuntimeException("No se encontro usuario en BD");
        if(c == null) throw new RuntimeException("No se encontro carruaje en BD");

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
        }else {
            throw new RuntimeException("El carruaje ya tiene un usuario asignado en BD");
        }

        return carruajeBus;
    }

    @Override
    public List<Carruaje> getListaDeCarruajes() {
        Session sesion = sessionFactory.getCurrentSession();

        Criteria criteria = sesion.createCriteria(Carruaje.class);

        return  criteria.list();
    }
}
