package com.tallerwebi.dominio;

import java.io.Serializable;
import java.util.Objects;

/**
 * Implementación de clave compuesta para SessionMonster.
 */
public class SessionMonsterIDImpl implements SessionMonsterID, Serializable {
    private static final long serialVersionUID = 1L;

    private Long sessionId;
    private Long monsterId;

    public SessionMonsterIDImpl() {}

    public SessionMonsterIDImpl(Long sessionId, Long monsterId) {
        this.sessionId = sessionId;
        this.monsterId = monsterId;
    }

    @Override
    public Long getSessionId() {
        return sessionId;
    }

    @Override
    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public Long getMonsterId() {
        return monsterId;
    }

    @Override
    public void setMonsterId(Long monsterId) {
        this.monsterId = monsterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionMonsterIDImpl that = (SessionMonsterIDImpl) o;
        return Objects.equals(sessionId, that.sessionId)
                && Objects.equals(monsterId, that.monsterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId, monsterId);
    }
}
