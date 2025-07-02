package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidades.Usuario;
import java.util.List;

public interface ServicioRanking {
    List<Usuario> obtenerRanking();
}