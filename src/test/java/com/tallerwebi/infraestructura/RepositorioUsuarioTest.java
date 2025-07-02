package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.interfaces.RepositorioUsuario;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})

public class RepositorioUsuarioTest {

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Test
    @Transactional
    @Rollback
    public void queSePuedaBuscarUnHeroePorId() {
        repositorioUsuario.guardar(new Usuario(1L,"u1"));
        repositorioUsuario.guardar(new Usuario(2L,"u2"));
        repositorioUsuario.guardar(new Usuario(3L,"u3"));

        Usuario usuario = repositorioUsuario.buscarUsuarioPorId(2L);

//        assertThat(usuario,notNullValue());
//        assertThat(usuario.getId(),notNullValue());
//        assertThat(usuario.getEmail(),equalTo("u2"));

    }

}
