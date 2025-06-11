package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioMonster {

    List<Monster> obtenerTodosLosMonstruos();

    Monster findById(Long monsterId);
}