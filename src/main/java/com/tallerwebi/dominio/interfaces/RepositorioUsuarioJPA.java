package com.tallerwebi.dominio.interfaces;

import com.tallerwebi.dominio.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioUsuarioJPA
        extends JpaRepository<Usuario, Long> {}
