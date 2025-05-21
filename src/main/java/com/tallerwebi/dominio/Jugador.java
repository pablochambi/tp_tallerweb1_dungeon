package com.tallerwebi.dominio;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Jugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(nullable = false)
    private Integer oro = 1000;

    @OneToMany(mappedBy = "jugador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Item> itemsComprados = new ArrayList<>();

    @ManyToMany
    private List<Item> inventario = new ArrayList<>();

    public Jugador() {}

    public Jugador(String nombre, int oro) {
        this.nombre = nombre;
        this.oro = oro;
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

    public int getOro() { return oro; }
    public void setOro(int oro) { this.oro = oro; }

    public List<Item> getInventario() { return inventario; }
    public void setInventario(List<Item> inventario) { this.inventario = inventario; }

    public List<Item> getItemsComprados() {
        return itemsComprados;
    }

    public void setItemsComprados(List<Item> itemsComprados) {
        this.itemsComprados = itemsComprados;
    }
}

