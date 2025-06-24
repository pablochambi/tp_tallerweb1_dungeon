package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.GameSession;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.interfaces.RepositorioSession;
import com.tallerwebi.dominio.interfaces.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RepositorioSessionImpl implements RepositorioSession {

    private final JdbcTemplate jdbc;
    private final RepositorioUsuario usuarioRepo;

    @Autowired
    public RepositorioSessionImpl(
            JdbcTemplate jdbc,
            RepositorioUsuario usuarioRepo) {
        this.jdbc = jdbc;
        this.usuarioRepo = usuarioRepo;
    }

    @Override
    public GameSession startNew() {
        // 1) Asegurar que exista el usuario
        Usuario u = usuarioRepo.buscarUsuarioPorId(1L);
        if (u == null) {
            u = new Usuario();
            u.setVida(100);
            u.setAtk(10);
            u.setDefensa(false);
            u.setOro(1000);
            usuarioRepo.guardar(u);
        }

        // 2) Insertar la nueva sesión con timestamp de inicio
        jdbc.update(
                "INSERT INTO game_session " +
                        "(usuario_id, turno, nivel, active, finished, started_at) " +
                        "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)",
                u.getId(), 1, 1, 1, 0
        );

        // 3) Recuperar el ID generado (HSQLDB CALL IDENTITY())
        Long newId = jdbc.queryForObject("CALL IDENTITY()", Long.class);

        // 4) Construir el objeto y devolverlo
        GameSession s = new GameSession();
        s.setid(newId);
        s.setUsuario(u);
        s.setTurno(1);
        s.setNivel(1);
        s.setActive(true);
        s.setFinished(false);
        return s;
    }

    @Override
    public GameSession findActive() {
        return jdbc.queryForObject(
                "SELECT id, usuario_id, turno, nivel, active, finished " +
                        "FROM game_session WHERE active = TRUE",
                (rs, rowNum) -> {
                    GameSession s = new GameSession();
                    s.setid       (rs.getLong  ("id"));
                    s.setUsuario  (usuarioRepo.buscarUsuarioPorId(
                            rs.getLong("usuario_id")));
                    s.setTurno    (rs.getInt   ("turno"));
                    s.setNivel    (rs.getInt   ("nivel"));
                    s.setActive   (rs.getInt   ("active")   == 1);
                    s.setFinished (rs.getInt   ("finished") == 1);
                    return s;
                }
        );
    }

    @Override
    public void save(GameSession s) {
        // Actualiza los campos editables y marca ended_at
        jdbc.update(
                "UPDATE game_session SET " +
                        " turno = ?, nivel = ?, active = ?, finished = ?, ended_at = CURRENT_TIMESTAMP " +
                        "WHERE id = ?",
                s.getTurno(),
                s.getNivel(),
                s.isActive()   ? 1 : 0,
                s.isFinished() ? 1 : 0,
                s.getId()
        );
    }

    @Override
    public void delete(GameSession s) {
        // 1) Borra primero los monstruos de la sesión
        jdbc.update(
                "DELETE FROM session_monster WHERE session_id = ?",
                s.getId()
        );
        // 2) Luego borra la sesión
        jdbc.update(
                "DELETE FROM game_session WHERE id = ?",
                s.getId()
        );
    }
}
