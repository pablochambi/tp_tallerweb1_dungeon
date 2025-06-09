package com.tallerwebi.dominio;

import javax.persistence.*;

@Entity
@Table(name = "session_monster")
@IdClass(SessionMonsterIDImpl.class)
public class SessionMonster {

    @Id
    private Long sessionId;

    @Id
    private Long monsterId;

    @Column(name = "vida_actual")
    private int vidaActual;

    private int orden;

    @ManyToOne
    @JoinColumn(name = "monsterId", insertable = false, updatable = false)
    private Monster monster;

    public SessionMonster() {}


    public Long getSessionId() {
        return sessionId;
    }
    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getMonsterId() {
        return monsterId;
    }
    public void setMonsterId(Long monsterId) {
        this.monsterId = monsterId;
    }

    public int getVidaActual() {
        return vidaActual;
    }
    public void setVidaActual(int vidaActual) {
        this.vidaActual = vidaActual;
    }

    public int getOrden() {
        return orden;
    }
    public void setOrden(int orden) {
        this.orden = orden;
    }

    public Monster getMonster() {
        return monster;
    }
    public void setMonster(Monster monster) {
        this.monster = monster;
    }
}