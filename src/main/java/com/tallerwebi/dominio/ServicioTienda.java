package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidades.Item;
import com.tallerwebi.dominio.entidades.Jugador;

import java.util.List;

public interface ServicioTienda {
    List<Item> obtenerItems();
    String comprarItem(Long itemId, Jugador jugador);
}