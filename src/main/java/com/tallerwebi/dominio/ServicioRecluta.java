package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioRecluta {
    //Integer getCantidadDeReclutasSemanales(Integer nivelDeCarrosa);

    //Integer getCantidadDeReclutasDisponiblesHoy(Integer nivelDeCarrosa, Integer totalHeroesReclutados, Integer numeroDeSemana);

    //Integer getHeroesReclutadosEstaSemana(Integer nivelRedDeLLegadas, Integer heroesReclutadosEstaSemana, Boolean pasoUnaSemana);

    CarruajeHeroe reclutarSegunIdHeroe(Long idHeroe);

    List<Heroe> getListaDeHeroesEnCarruaje();

    void setHeroeEnCarruaje(Heroe heroe, Carruaje carruaje);

    Integer getCantidadDeHeroesDisponiblesEnCarruaje(Carruaje carruaje);

    void mostrarListaDeHeroesEnCarruaje(Carruaje carruaje);

    Carruaje getAsignarCarruajeAUsuario(Usuario usuario);

    Integer getNumeroDeSemanaSegunCarruaje(Carruaje carruaje);

    Integer getNivelSegunCarruaje(Carruaje carruaje);

    Integer getCantidadDeHeroesSemanalesSegunCarruaje(Carruaje carruaje);

    Heroe reclutarUnHeroeAleatorio(Carruaje carruaje);

    void pasarSemana(Carruaje carruaje);

    void setHeroe(Heroe heroe);

    void aumentarNivel(Carruaje carruaje);

    Heroe getHeroeSegunID(long l);

    void agregarCarruaje(Carruaje carruaje);

    void setSemanaEnUsuarioSemanaSegunCarruaje(Carruaje carruaje);

    void mostrarListaDeHeroesDelUsuario(Carruaje carruaje);


    Integer getCantidadDeHeroesEnUsuarioHeroeSegunCarruaje(Carruaje carruaje);

    Carruaje obtenerCarruajePorUsuario(Usuario usuario);

    Integer getCantidadDeHeroesEnUsuarioHeroeSegunUsuario(Usuario usuario);
}
