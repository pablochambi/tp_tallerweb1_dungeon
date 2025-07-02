package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.interfaces.RepositorioUsuario;
import com.tallerwebi.dominio.ServicioRanking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioRankingImpl implements ServicioRanking {

    private final RepositorioUsuario usuarioRepo;

    @Autowired
    public ServicioRankingImpl(RepositorioUsuario usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    @Override
    public List<Usuario> obtenerRanking() {
        return usuarioRepo.obtenerRankingJugadores();
    }
}