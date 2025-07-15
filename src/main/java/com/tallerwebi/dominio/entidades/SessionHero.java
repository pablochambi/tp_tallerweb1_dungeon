package com.tallerwebi.dominio.entidades;

import javax.persistence.*;

@Entity
@Table(name = "session_hero")
public class SessionHero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private GameSession session;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "hero_id", nullable = false)
    private Heroe hero;

    @Column(name = "vida_actual", nullable = false)
    private int vidaActual;

    @Column(name = "atk_actual", nullable = false)
    private int atkActual;

    @Column(nullable = false)
    private int orden;

    @Column(nullable = false)
    private boolean defending = false;


    public SessionHero() {}

    // --- Getters & Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GameSession getSession() {
        return session;
    }

    public void setSession(GameSession session) {
        this.session = session;
    }

    public Heroe getHero() {
        return hero;
    }

    public void setHero(Heroe hero) {
        this.hero = hero;
    }

    public int getVidaActual() {
        return vidaActual;
    }

    public int getAtkActual() {return atkActual;}

    public void setAtkActual(int atkActual) {this.atkActual = atkActual;}

    public void setVidaActual(int vidaActual) {
        this.vidaActual = vidaActual;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public boolean isDefending() {
        return defending;
    }

    public void setDefending(boolean defending) {
        this.defending = defending;
    }

    // --- Logica de combate ---

//    //public int damageOutput() {
//        return hero.getAtk();
//    }

    public int damageOutput() {
        return this.atkActual;
    }

    public void takeDamage(int damage) {
        // defensa base que el héroe tiene
        int mitigated = hero.getDefensaBase();
        int dmg = Math.max(0, damage - mitigated);
        if (defending) {
            dmg /= 2;
            defending = false;
        }
        vidaActual = Math.max(0, vidaActual - dmg);
    }

    public void defend() {
        this.defending = true;
    }

    public void heal(int amount) {
        int max = hero.getMaxVida();
        vidaActual = Math.min(max, vidaActual + amount);
    }

    public void increaseDamage(double porcentaje) {
      int incremento= (int) (hero.getAtk() * porcentaje);
      this.atkActual += incremento;
    }
}
