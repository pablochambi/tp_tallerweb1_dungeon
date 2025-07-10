package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Heroe;
import com.tallerwebi.dominio.entidades.Usuario;

import java.util.List;

public interface ServicioSanatorio {
    List<Heroe> obtenerHeroesCurables(Usuario usuario);
    void curarHeroe(Usuario usuario, Long idHeroe);
}