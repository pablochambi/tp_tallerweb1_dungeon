package com.tallerwebi.dominio.interfaces;

import com.tallerwebi.dominio.entidades.GameSession;
import com.tallerwebi.dominio.entidades.Monster;

import java.util.List;

public interface RepositorioMonster {

    List<Monster> obtenerTodosLosMonstruos();

    Monster findById(Long monsterId);

}