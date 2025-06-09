package com.tallerwebi.dominio;

public class UsuarioHeroe {

    private Usuario usuario;
    private Heroe heroe;
    private Integer nivel;

    public UsuarioHeroe(Usuario usuario, Heroe heroe) {
        this.usuario = usuario;
        this.heroe = heroe;
        this.nivel = heroe.getNivel();
    }


    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Heroe getHeroe() {
        return heroe;
    }

    public void setHeroe(Heroe heroe) {
        this.heroe = heroe;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }
}
