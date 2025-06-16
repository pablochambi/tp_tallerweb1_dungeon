package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.GameSession;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.interfaces.RepositorioSession;
import com.tallerwebi.dominio.interfaces.RepositorioUsuario;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class RepositorioSessionImpl implements RepositorioSession {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private RepositorioUsuario usuarioRepositorio;

    @Override
    public GameSession startNew() {
        // 1) Asegurar Jugador existente en BDD
        Usuario usuario = usuarioRepositorio.buscarUsuarioPorId(1L);
        if (usuario == null) {
            usuario = new Usuario();
            usuario.setVida(100);
            usuario.setAtk(10);
            usuario.setDefensa(false);
            usuario.setOro(1000);
            usuarioRepositorio.guardar(usuario);
        }
        // 2) Crear y guardar la sesión ya ligada a ese jugador
        GameSession session = new GameSession();
        session.setUsuario(usuario);
        sessionFactory.getCurrentSession().save(session);
        return session;
    }

    @Override
    public GameSession findActive() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM GameSession g WHERE g.active = true", GameSession.class)
                .uniqueResult();
    }

    @Override
    public void save(GameSession s) {
        sessionFactory.getCurrentSession().saveOrUpdate(s);
    }

    @Override
    public void delete(GameSession session) {
        // 1) Eliminar primero los SessionMonster asociados a esta sesión
        sessionFactory
                .getCurrentSession()
                .createQuery("delete from SessionMonster sm where sm.sessionId = :sid")
                .setParameter("sid", session.getSessionId())
                .executeUpdate();

        // 2) Ahora eliminar la GameSession
        sessionFactory.getCurrentSession().delete(session);
    }
}
