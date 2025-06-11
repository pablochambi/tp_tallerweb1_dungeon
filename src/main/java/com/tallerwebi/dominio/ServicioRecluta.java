package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidades.Carruaje;
import com.tallerwebi.dominio.entidades.Heroe;
import com.tallerwebi.dominio.entidades.Usuario;

import java.util.List;

public interface ServicioRecluta {

    List<Heroe> getListaDeHeroesEnCarruaje(Carruaje carruaje);

    List<Heroe> getHeroesDisponiblesEnCarruaje(Carruaje carruaje);

    List<Heroe> getHeroesObtenidosPorElUsuario(Usuario usuario);

    void quitarUnHeroeDelCarruaje(Long idHeroe, Carruaje carruaje);

    void agregarUnHeroeAlUsuario(Long idHeroe, Usuario usuario);

    List<Heroe> getHeroesDisponiblesEnUsuario(Long idUsuario);

    void setRelacionEntreCarruajeYHeroe(Long idCarruaje, Long idHeroe);

    Carruaje asignarOActualizarUnCarrujeAUnUsuario(Long idUsuario);

    Usuario registrarUnUsuario(Usuario usuario);

    Usuario getUsuarioRegistradoPorId(Long idUsuario);

    Carruaje getCarruajeDelUsuarioPorId(Long idUsuario);

    Usuario getUsuarioPorId(Long idUsuario);

    Carruaje getCarruajeDelUsuarioId(Long idUsuario);

}
