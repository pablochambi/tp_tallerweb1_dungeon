package com.tallerwebi.dominio.entidades;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "game_session")
public class GameSession {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public void setid(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

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

    @CreationTimestamp
    @Column(name="started_at", nullable = false, updatable = false)
    private LocalDateTime startedAt;

    @UpdateTimestamp
    @Column(name="ended_at")
    private LocalDateTime endedAt;

    public GameSession() {}

    public Long getId() { return id; }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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

    public Object getSessionId() {
        return null;
    }

    public LocalDateTime getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

}
