package com.tallerwebi.dominio;

public class Heroe {

    private Long id;
    private String nombre;
    private Integer nivel;

    public Heroe(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;

        this.nivel = 1;
    }

    public Long getId() {
        return id;
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

    public void mostrarHeroe() {
        System.out.print("Heroe\n");
        System.out.printf("ID: %d,   Nom: %s,   Niv: %d\n", getId(),getNombre(),getNivel());
    }
}
