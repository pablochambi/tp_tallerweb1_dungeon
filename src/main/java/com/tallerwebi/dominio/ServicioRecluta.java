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

//    CarruajeHeroe reclutarSegunIdHeroe(Long idHeroe);

//
//    void setHeroeEnCarruaje(Heroe heroe, Carruaje carruaje);

//    Integer getCantidadDeHeroesDisponiblesEnCarruaje(Carruaje carruaje);

//    void mostrarListaDeHeroesEnCarruaje(Carruaje carruaje);
//
//    Carruaje getAsignarCarruajeAUsuario(Usuario usuario);
//
//    Integer getNumeroDeSemanaSegunCarruaje(Carruaje carruaje);
//
//    Integer getNivelSegunCarruaje(Carruaje carruaje);
//
//    Integer getCantidadDeHeroesSemanalesSegunCarruaje(Carruaje carruaje);
//
//    Heroe reclutarUnHeroeAleatorio(Carruaje carruaje);
//
//    void pasarSemana(Carruaje carruaje);
//
//    void setHeroe(Heroe heroe);
//
//    void aumentarNivel(Carruaje carruaje);
//
//    Heroe getHeroeSegunID(long l);
//
//    void agregarCarruaje(Carruaje carruaje);
//
//    void setSemanaEnUsuarioSemanaSegunCarruaje(Carruaje carruaje);
//
//    void mostrarListaDeHeroesDelUsuario(Carruaje carruaje);
//
//
//    Integer getCantidadDeHeroesEnUsuarioHeroeSegunCarruaje(Carruaje carruaje);
//
//    Carruaje obtenerCarruajePorUsuario(Usuario usuario);

//    Integer getCantidadDeHeroesEnUsuarioHeroeSegunUsuario(Usuario usuario);


}
