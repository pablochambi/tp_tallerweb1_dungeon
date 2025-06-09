package com.tallerwebi.dominio;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "jugador")
public class Jugador {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private int vida;
    private int atk;
    private boolean defensa;
    @Column(nullable = false)
    private  Integer oro = 1000;
    @OneToMany(mappedBy = "jugador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Item> itemsComprados = new ArrayList<>();
    @ManyToMany
    private List<Item> inventario = new ArrayList<>();
    public Jugador(String jugador1, int i) {}

    public Jugador() {

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOro() {
        return oro;
    }

    public void setOro(Integer oro) {
        this.oro = oro;
    }

    public List<Item> getInventario() {
        return inventario;
    }

    public void setInventario(List<Item> inventario) {
        this.inventario = inventario;
    }

    public List<Item> getItemsComprados() {
        return itemsComprados;
    }

    public void setItemsComprados(List<Item> itemsComprados) {
        this.itemsComprados = itemsComprados;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public boolean isDefensa() {
        return defensa;
    }

    public void setDefensa(boolean defensa) {
        this.defensa = defensa;
    }
}
