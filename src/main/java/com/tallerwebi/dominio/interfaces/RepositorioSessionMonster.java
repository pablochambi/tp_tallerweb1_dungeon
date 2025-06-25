package com.tallerwebi.dominio.interfaces;

import com.tallerwebi.dominio.entidades.SessionMonster;
import com.tallerwebi.dominio.entidades.GameSession;
import com.tallerwebi.dominio.entidades.Monster;

import java.util.Collection;
import java.util.List;

public interface RepositorioSessionMonster {
    void add(GameSession s, Monster m);
    void add(GameSession session, Monster monster, int dungeonNumber);
    List<SessionMonster> findBySession(GameSession session);
    void update(SessionMonster sm);
    void deleteBySession(GameSession session);

    void deleteBySessionAndDungeonNumber(GameSession session, int dungeonNumber);

    List<SessionMonster> findBySessionAndExpeditionNumber(GameSession session, int expeditionNumber);


    void deleteBySessionAndExpeditionNumber(GameSession session, int i);
}