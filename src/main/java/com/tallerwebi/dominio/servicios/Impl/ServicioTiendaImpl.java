package com.tallerwebi.dominio.servicios.Impl;

import com.tallerwebi.dominio.entidades.Inventario;
import com.tallerwebi.dominio.entidades.Item;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.interfaces.RepositorioItem;
import com.tallerwebi.dominio.interfaces.RepositorioUsuario;
import com.tallerwebi.dominio.interfaces.RepositorioInventario;
import com.tallerwebi.dominio.servicios.ServicioTienda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ServicioTiendaImpl implements ServicioTienda {


    private RepositorioUsuario repositorioUsuario;
    private RepositorioItem repositorioItem;
    private RepositorioInventario repositorioInventario;

    @Autowired
    public ServicioTiendaImpl(RepositorioItem repositorioItem, RepositorioUsuario repositorioUsuario, RepositorioInventario repositorioInventario) {
        this.repositorioItem = repositorioItem;
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioInventario = repositorioInventario;
    }

    @Override
    public void comprar(Usuario usuarioSesion, Long idItem) {
        Usuario usuario = repositorioUsuario.buscarUsuarioPorId(usuarioSesion.getId());
        if (usuario == null) {throw new RuntimeException("Usuario no encontrado");
        }

        Item itemTienda = repositorioItem.buscarPorId(idItem);
        if (itemTienda == null) {throw new RuntimeException("Item no encontrado");}

        if (usuario.getOro() < itemTienda.getPrecio()){throw new RuntimeException("No tenes suficiente oro!");}

        usuario.setOro(usuario.getOro() - itemTienda.getPrecio());

        if (usuario.getInventario() == null) {
            Inventario nuevoInventario = new Inventario();
            nuevoInventario.setUsuario(usuario);
            repositorioInventario.guardar(nuevoInventario);
            usuario.setInventario(nuevoInventario);
            repositorioUsuario.modificar(usuario);
        }

        // Clonar item para inventario
        Item itemComprado = new Item();
        itemComprado.setNombre(itemTienda.getNombre());
        itemComprado.setTipo(itemTienda.getTipo());
        itemComprado.setPrecio(itemTienda.getPrecio());
        itemComprado.setInventario(usuario.getInventario());

        repositorioItem.guardarItem(itemComprado);
        repositorioUsuario.modificar(usuario);
    }

    @Override
    public List<Item> obtenerItemsPorInventario(Long idInventario) {
        return repositorioItem.obtenerLosItemsByInventario(idInventario);
    }

    @Override
    public List<Item> obtenerItemsDeUsuario(Usuario usuario) {
        if (usuario.getInventario() == null) return new ArrayList<>();
        return repositorioItem.obtenerLosItemsByInventario(usuario.getInventario().getId());
    }

    @Override
    public List<Item> obtenerTodosLosItems() {
        return repositorioItem.obtenerTodosLosItems();
    }

    @Override
    public List<Item> obtenerItemsDeTienda() {
        return repositorioItem.obtenerItemsSinInventario();
    }
//
//    private RepositorioUsuario repositorioUsuario;
//    private RepositorioItem repositorioItem;
//
//    @Autowired
//    public ServicioTiendaImpl(RepositorioItem repositorioItem, RepositorioUsuario repositorioUsuario) {
//         this.repositorioItem = repositorioItem;
//         this.repositorioUsuario = repositorioUsuario;
//    }
//
//    @Override
//    public void comprar(Usuario usuarioSesion, Long idItem) {
//
//       Usuario usuario =  repositorioUsuario.buscarUsuarioPorId(usuarioSesion.getId());
//       if (usuario == null) {
//           throw new RuntimeException("Usuario no encontrado");
//       }
//
//       Item item = repositorioItem.buscarPorId(idItem);
//       if (item == null) {
//           throw new RuntimeException("Item no encontrado");
//       }
//
//       if (usuario.getOro() < item.getPrecio()) {
//           throw new RuntimeException("No tenes suficiente oro!");
//       }
//
//       usuario.setOro(usuario.getOro() - item.getPrecio());
//
//       Inventario inventario = usuario.getInventario();
//       item.setInventario(inventario);
//       repositorioItem.guardarItem(item);
//
//       repositorioUsuario.modificar(usuario);
//
//    }
//
//    @Override
//    public List<Item> obtenerItemsPorInventario(Long idInventario) {
//        return repositorioItem.obtenerLosItemsByInventario(idInventario);
//    }
//
//    @Override
//    public List<Item> obtenerItemsDeUsuario(Usuario usuario) {
//        return repositorioItem.obtenerLosItemsByInventario(usuario.getInventario().getId());
//    }
//
//    @Override
//    public List<Item> obtenerTodosLosItems() {
//        return repositorioItem.obtenerTodosLosItems();
//    }



}
