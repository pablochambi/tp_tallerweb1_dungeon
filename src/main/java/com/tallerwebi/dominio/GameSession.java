package com.tallerwebi.dominio;

import javax.persistence.*;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "game_session")
public class GameSession {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long id;


    @ManyToOne
    @JoinColumn(name = "jugador_id", nullable = false)
    private Jugador jugador;

    @Column(nullable = false)
    private int turno = 1;

    @Column(nullable = false)
    private int nivel = 1;

    @Column(name="active", nullable = false, columnDefinition = "INTEGER DEFAULT 1")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean active = true;

    @Column(name="finished", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean finished = false;

    public GameSession() {}

    public Long getId() { return id; }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public int getTurno() {
        return turno;
    }

    public void setTurno(int turno) {
        this.turno = turno;
    }

    public int getNivel() {
        return nivel;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }


    public void setNivel(int i) {
        this.nivel = i;
    }
}
