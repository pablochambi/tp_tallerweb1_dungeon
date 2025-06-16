package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidades.Item;
import com.tallerwebi.dominio.entidades.Usuario;

import java.util.List;

public interface ServicioTienda {
    List<Item> obtenerItems();
    String comprarItem(Long itemId, Usuario usuario);
}