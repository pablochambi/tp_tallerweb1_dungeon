package com.tallerwebi.dominio.interfaces;
import com.tallerwebi.dominio.entidades.Inventario;
import com.tallerwebi.dominio.entidades.Item;

import java.util.List;

public interface RepositorioItem {
    List<Item> obtenerTodosLosItems();
    Item buscarPorId(Long id);



    void guardarItem(Item item);

    List<Item> obtenerLosItemsByInventario(Long idInventario);
}
