package com.tallerwebi.dominio;
import  com.tallerwebi.dominio.Item;

import java.util.List;

public interface RepositorioItem {
    List<Item> obtenerTodosLosItems();

    Item obtenerPorId(Long itemId);
}
