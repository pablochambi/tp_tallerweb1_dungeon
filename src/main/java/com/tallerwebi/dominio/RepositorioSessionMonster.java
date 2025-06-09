package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioSessionMonster {
    void add(GameSession s, Monster m);
    List<SessionMonster> findBySession(GameSession s);
    void update(SessionMonster sm);
}