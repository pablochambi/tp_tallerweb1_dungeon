package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Inventario;
import com.tallerwebi.dominio.entidades.Item;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.interfaces.RepositorioItem;
import com.tallerwebi.dominio.interfaces.RepositorioUsuario;
import com.tallerwebi.dominio.interfaces.RepositorioInventario;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
public class RepositorioItemTest {

    @Autowired
    RepositorioItem repositorioItem;
    @Autowired
    RepositorioUsuario repositorioUsuario;
    @Autowired
    RepositorioInventario repositorioInventario;


    @Test
    @Transactional
    @Rollback
    public void queSeGuardeElItem() {
        Item item = new Item();
        repositorioItem.guardarItem(item);

        assertNotNull(item.getId());
    }

    @Test
    @Transactional
    @Rollback
    public void obtenerListaDeItemsPorInventario() {

        Inventario inventario = new Inventario();
        repositorioInventario.guardar(inventario);
        Usuario usuario = new Usuario();
        usuario.setOro(500);
        usuario.setEmail("test2@prueba.com");
        usuario.setPassword("1234");
        usuario.setInventario(inventario);

        repositorioUsuario.guardar(usuario);

        // Crear dos items
        Item item1 = new Item();
        item1.setNombre("Espada");
        item1.setPrecio(150);
        item1.setInventario(usuario.getInventario());

        Item item2 = new Item();
        item2.setNombre("Escudo");
        item2.setPrecio(120);
        item2.setInventario(usuario.getInventario());

        repositorioItem.guardarItem(item1);
        repositorioItem.guardarItem(item2);

        List<Item> items = repositorioItem.obtenerLosItemsByInventario(inventario.getId());

        assertNotNull(items);
        assertEquals(2, items.size());

    }

    @Test
    @Transactional
    @Rollback
    public void buscarPorIdDevuelveElItemEsperado() {

        Inventario inventario = new Inventario();
        Item item = new Item();
        item.setId(1L);
        item.setNombre("Poción");
        item.setPrecio(100);
        item.setInventario(inventario);

        repositorioItem.guardarItem(item);

        assertNotNull(repositorioItem.buscarPorId(item.getId()));
        assertEquals("Poción", repositorioItem.buscarPorId(item.getId()).getNombre());
    }


}
