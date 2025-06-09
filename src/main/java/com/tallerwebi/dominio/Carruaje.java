package com.tallerwebi.dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Carruaje {
    private Long id = 0L;
    private Integer nivel;
    private Integer semana;
    private Integer cantidadDeHeroesSemanales;
    private Integer cantHeroesDisponibles;
    private Usuario usuario;

    public Carruaje() {
        this.id++;
        this.nivel = 0;
        this.semana = 0;
        this.cantidadDeHeroesSemanales = 2;
    }

//    public void reclutarUnHeroeDisponibleAleatorio() {
//        List<Heroe> disponibles = obtenerHeroesDisponibles("enCarruaje");
//
//        if (!disponibles.isEmpty()) {
//            Heroe seleccionado = seleccionarUnHeroeAleatorio(disponibles);
//            seleccionado.setEstado("reclutado");
//        }
//
//    }

    private  Heroe seleccionarUnHeroeAleatorio(List<Heroe> disponibles) {
        Random random = new Random();
        Heroe seleccionado = disponibles.get(random.nextInt(disponibles.size()));
        return seleccionado;
    }


    public void aumentarNivel() {
        this.nivel++;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public Integer getSemana() {
        return semana;
    }

    public void setSemana(Integer semana) {
        this.semana = semana;
    }


    public Integer getCantidadDeHeroesSemanales() {
        return this.cantidadDeHeroesSemanales + this.nivel;
    }

    public void setCantidadDeHeroesSemanales(Integer cantidadDeHeroesSemanales) {
        this.cantidadDeHeroesSemanales = cantidadDeHeroesSemanales;
    }

    public void pasaUnaSemana() {
        this.semana++;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
