package com.tallerwebi.dominio.entidades;

import com.tallerwebi.dominio.SessionMonsterIDImpl;

import javax.persistence.*;

@Entity
@Table(name = "session_monster")
@IdClass(SessionMonsterIDImpl.class)
public class SessionMonster {

    @Id
    @Column(name = "session_id")
    private Long sessionId;

    @Id
    @Column(name = "monster_id")
    private Long monsterId;

    @Column(name = "vida_actual")
    private int vidaActual;

    private int orden;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "monster_id",
            referencedColumnName = "id",
            insertable = false, //al poner false le digo a Hibernate q este campo se rellena solo desde SQL
            updatable = false
    )
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

    public String getImagen() {
        return monster.getImagen();
    }


    public String getNombre() {
        return monster.getNombre();
    }

    public Monster getMonster() {
        return monster;
    }
    public void setMonster(Monster monster) {
        this.monster = monster;
    }
}