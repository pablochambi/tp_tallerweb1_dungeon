package com.tallerwebi.dominio.entidades;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
public class CarruajeHeroe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Carruaje carruaje;

    @ManyToOne
    private Heroe heroe;


    public CarruajeHeroe(Carruaje carruaje, Heroe heroe) {
        this.carruaje = carruaje;
        this.heroe = heroe;
    }

    public CarruajeHeroe() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setHeroe(Heroe heroe1) {
    }

    public void setCarruaje(Carruaje carruajeMock) {

    }
}
