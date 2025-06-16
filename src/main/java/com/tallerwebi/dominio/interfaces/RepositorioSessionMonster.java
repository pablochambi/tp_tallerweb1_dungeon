package com.tallerwebi.dominio.interfaces;

import com.tallerwebi.dominio.entidades.SessionMonster;
import com.tallerwebi.dominio.entidades.GameSession;
import com.tallerwebi.dominio.entidades.Monster;

import java.util.List;

public interface RepositorioSessionMonster {
    void add(GameSession s, Monster m);
    List<SessionMonster> findBySession(GameSession s);
    void update(SessionMonster sm);

    void deleteBySession(GameSession session);
}