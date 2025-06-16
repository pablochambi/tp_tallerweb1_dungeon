package com.tallerwebi.dominio.interfaces;

import com.tallerwebi.dominio.entidades.GameSession;

public interface RepositorioSession {
    GameSession startNew();
    GameSession findActive();
    void save(GameSession s);

    void delete(GameSession session);
}