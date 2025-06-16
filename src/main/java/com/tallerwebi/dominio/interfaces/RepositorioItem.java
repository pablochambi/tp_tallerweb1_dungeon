package com.tallerwebi.dominio.interfaces;
import com.tallerwebi.dominio.entidades.Item;

import java.util.List;

public interface RepositorioItem {
    List<Item> obtenerTodosLosItems();
    Item buscarPorId(Long id);

    List<Item> listarTodos();
}
