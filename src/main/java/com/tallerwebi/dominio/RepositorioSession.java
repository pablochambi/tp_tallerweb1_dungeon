package com.tallerwebi.dominio;

public interface RepositorioSession {
    GameSession startNew();
    GameSession findActive();
    void save(GameSession s);
}