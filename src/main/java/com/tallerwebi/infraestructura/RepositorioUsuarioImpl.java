package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.interfaces.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RepositorioUsuarioImpl implements RepositorioUsuario {

    private final JdbcTemplate jdbc;

    @Autowired
    public RepositorioUsuarioImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Usuario buscarUsuario(String email, String password) {
        try {
            return jdbc.queryForObject(
                    "SELECT id, email, password, rol, activo, nombre, vida, atk, defensa, oro " +
                            "FROM Usuario WHERE email = ? AND password = ?",
                    this::mapRowToUsuario,
                    email, password
            );
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public void guardar(Usuario u) {
        jdbc.update(
                "INSERT INTO Usuario " +
                        "(email, password, rol, vida, atk, defensa, oro) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)",
                u.getEmail(),
                u.getPassword(),
                u.getRol(),
                u.getVida(),
                u.getAtk(),
                u.isDefensa(),
                u.getOro()
        );
        // Capturamos el ID generado
        Long newId = jdbc.queryForObject("CALL IDENTITY()", Long.class);
        u.setId(newId);
    }

    @Override
    public Usuario buscar(String email) {
        try {
            return jdbc.queryForObject(
                    "SELECT id, email, password, rol, activo, nombre, vida, atk, defensa, oro " +
                            "FROM Usuario WHERE email = ?",
                    this::mapRowToUsuario,
                    email
            );
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public void modificar(Usuario u) {
        jdbc.update(
                "UPDATE Usuario SET " +
                        " email    = ?, " +
                        " password = ?, " +
                        " rol      = ?, " +
                        " vida     = ?, " +
                        " atk      = ?, " +
                        " defensa  = ?, " +
                        " oro      = ? " +
                        "WHERE id = ?",
                u.getEmail(),
                u.getPassword(),
                u.getRol(),
                u.getVida(),
                u.getAtk(),
                u.isDefensa(),
                u.getOro(),
                u.getId()
        );
    }

    @Override
    public Usuario buscarUsuarioPorId(Long id) {
        try {
            return jdbc.queryForObject(
                    "SELECT id, email, password, rol, activo, nombre, vida, atk, defensa, oro " +
                            "FROM Usuario WHERE id = ?",
                    this::mapRowToUsuario,
                    id
            );
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    // Mapea un ResultSet a la entidad Usuario
    private Usuario mapRowToUsuario(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        Usuario u = new Usuario();
        u.setId       (rs.getLong   ("id"));
        u.setEmail    (rs.getString ("email"));
        u.setPassword (rs.getString ("password"));
        u.setRol      (rs.getString ("rol"));
        u.setActivo   (rs.getBoolean("activo"));
        u.setVida     (rs.getInt    ("vida"));
        u.setAtk      (rs.getInt    ("atk"));
        u.setDefensa  (rs.getBoolean("defensa"));
        u.setOro      (rs.getInt    ("oro"));
        return u;
    }
}

