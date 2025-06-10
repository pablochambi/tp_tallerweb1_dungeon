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
    private Integer precio;
    private String urlImagen;

    public Heroe(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.nivel = 1;
    }

    public Heroe(Long id, String nombre,Integer nivel,Integer precio,String urlImagen) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.urlImagen = urlImagen;
        this.nivel = nivel;
    }

    public Heroe() {

    }

    public void mostrarHeroe() {
        System.out.print("Heroe\n");
        System.out.printf("ID: %d,   Nom: %s,   Niv: %d\n", getId(),getNombre(),getNivel());
    }

}
