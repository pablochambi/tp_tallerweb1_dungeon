package com.tallerwebi.dominio.interfaces;

import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.entidades.Carruaje;

public interface RepositorioCarruaje {
    Carruaje buscarCarruajePorId(Long id);

    Carruaje guardar(Carruaje carruaje);

    Carruaje buscarCarruajeAsignadoAUnUsuario(Usuario usuarioBuscado);

    Carruaje asignarUsuarioAUnCarruje(Carruaje carruaje, Usuario usuarioBuscado);
}
