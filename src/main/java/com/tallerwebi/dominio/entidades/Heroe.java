package com.tallerwebi.dominio.entidades;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class Heroe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private Integer nivel;
    private String urlImagen;
    private int precio;

    public Heroe(Long id, String nombre, int precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.nivel = 1;
    }

    public Heroe(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.urlImagen = urlImagen;
        this.nivel = nivel;
        this.precio = precio;
    }

    public Heroe(int precio) {

        this.precio = precio;
    }

    public Heroe(long l, String cruzado, int i, int i1, String s) {
    }

    public Heroe(){};

    public void mostrarHeroe() {
        System.out.print("Heroe\n");
        System.out.printf("ID: %d,   Nom: %s,   Niv: %d\n", getId(),getNombre(),getNivel());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public String getUrlImagen() {
        return urlImagen;
    }
}
