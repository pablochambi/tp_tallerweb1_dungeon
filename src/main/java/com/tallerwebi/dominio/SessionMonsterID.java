package com.tallerwebi.dominio;
//JPA ME PIDE QUE IDCLASS SEA UNA CLASE SEPARADA
//CLAVE COMPUESTA DE SESSIONMONSTER
public interface SessionMonsterID {
    Long getSessionId();
    void setSessionId(Long sessionId);
    Long getMonsterId();
    void setMonsterId(Long monsterId);
}