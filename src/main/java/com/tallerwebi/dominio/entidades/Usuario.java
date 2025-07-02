package com.tallerwebi.dominio.entidades;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String email;
    private String password;
    private String rol;
    private int expedicionActual;
    private int mazmorraActual;
    private Boolean activo = false;
    @Column(nullable = false)
    private Integer oro = 1000;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Item> itemsComprados = new ArrayList<>();

    @ManyToMany
    private List<Item> inventario = new ArrayList<>();

    public Usuario(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public Usuario(){}



    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRol() {
        return rol;
    }
    public void setRol(String rol) {
        this.rol = rol;
    }
    public Boolean getActivo() {
        return activo;
    }
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public boolean activo() {
        return activo;
    }

    public void activar() {
        activo = true;
    }

    public List<Item> getItemsComprados() { return itemsComprados; }
    public void setItemsComprados(List<Item> l) { this.itemsComprados = l; }
    public List<Item> getInventario() { return inventario; }
    public void setInventario(List<Item> l) { this.inventario = l; }


    public Integer getOro() {
        return oro;
    }

    public void setOro(Integer oro) {
        this.oro = oro;
    }

    public boolean isActivo() {
        return Boolean.TRUE.equals(activo);
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getExpedicionActual() {
        return expedicionActual;
    }

    public void setExpedicionActual(int expedicionActual) {
        this.expedicionActual = expedicionActual;
    }

    public int getMazmorraActual() {
        return mazmorraActual;
    }

    public void setMazmorraActual(int mazmorraActual) {
        this.mazmorraActual = mazmorraActual;
    }


/*
    //SETTER Y GETTER QUE DEBERIAN SER DE HEROE
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
*/
}
