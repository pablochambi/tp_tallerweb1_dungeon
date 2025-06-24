package com.tallerwebi.dominio.entidades;

import com.tallerwebi.dominio.enums.TipoDeItem;

import javax.persistence.*;

@Entity
@Table(name = "items")
public class Item {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private TipoDeItem tipo;
    private Integer precio;

    @ManyToOne
    private Inventario inventario;


    public Item() {}

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

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public TipoDeItem getTipo() {
        return tipo;
    }

    public void setTipo(TipoDeItem tipo) {
        this.tipo = tipo;
    }

    public Inventario getInventario() {
        return inventario;
    }

    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }
}
