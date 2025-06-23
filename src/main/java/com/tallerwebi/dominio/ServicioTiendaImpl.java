package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidades.Item;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.interfaces.RepositorioItem;
import com.tallerwebi.dominio.interfaces.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ServicioTiendaImpl implements ServicioTienda {

    private final RepositorioItem itemRepo;
    private final RepositorioUsuario usuarioRepo;

    public ServicioTiendaImpl(RepositorioItem itemRepo, RepositorioUsuario usuarioRepo) {
        this.itemRepo = itemRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @Override
    public List<Item> obtenerItems() {
        return itemRepo.listarTodos();
    }

    @Override
    public String comprarItem(Long itemId, Usuario usuario) {
        Item item = itemRepo.buscarPorId(itemId);
        if (item == null) {
            return "Ítem no encontrado.";
        }

        if (usuario.getOro() < item.getPrecio()) {
            return "No tenés suficiente oro para comprar " + item.getNombre() + ".";
        }

        usuario.setOro(usuario.getOro() - item.getPrecio());
        usuario.getInventario().add(item);
        usuarioRepo.guardar(usuario);

        return "Compraste " + item.getNombre() + " por " + item.getPrecio() + " de oro.";
    }
}
