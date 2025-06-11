package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidades.Item;
import com.tallerwebi.dominio.entidades.Jugador;
import com.tallerwebi.dominio.interfaces.RepositorioItem;
import com.tallerwebi.dominio.interfaces.RepositorioJugador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ServicioTiendaImpl implements ServicioTienda {

    @Autowired private RepositorioItem itemRepo;
    @Autowired private RepositorioJugador jugadorRepo;

    @Override
    public List<Item> obtenerItems() {
        return itemRepo.listarTodos();
    }

    @Override
    public String comprarItem(Long itemId, Jugador jugador) {
        Item item = itemRepo.buscarPorId(itemId);
        if (item == null) {
            return "Ítem no encontrado.";
        }

        if (jugador.getOro() < item.getPrecio()) {
            return "No tenés suficiente oro para comprar " + item.getNombre() + ".";
        }

        jugador.setOro(jugador.getOro() - item.getPrecio());
        jugador.getInventario().add(item);
        jugadorRepo.save(jugador);

        return "Compraste " + item.getNombre() + " por " + item.getPrecio() + " de oro.";
    }
}
