package com.tallerwebi.dominio.interfaces;

import com.tallerwebi.dominio.entidades.Heroe;

public interface RepositorioHeroe {
    Heroe buscarHeroePorId(Long id);

    Heroe guardar(Heroe heroe);
}
