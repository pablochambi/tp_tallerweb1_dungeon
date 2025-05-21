package com.tallerwebi.dominio;

public class CarruajeHeroe {
    private Carruaje carruaje;
    private Heroe heroe;

    public CarruajeHeroe(Carruaje carruaje, Heroe heroe) {
        this.carruaje = carruaje;
        this.heroe = heroe;
    }

    public Carruaje getCarruaje() {
        return carruaje;
    }

    public void setCarruaje(Carruaje carruaje) {
        this.carruaje = carruaje;
    }

    public Heroe getHeroe() {
        return heroe;
    }

    public void setHeroe(Heroe heroe) {
        this.heroe = heroe;
    }
}
