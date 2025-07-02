package com.tallerwebi.dominio.servicios.Impl;

import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.interfaces.RepositorioUsuario;
import com.tallerwebi.dominio.servicios.ServicioLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service("servicioLogin")
@Transactional
public class ServicioLoginImpl implements ServicioLogin {

    private RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioLoginImpl(RepositorioUsuario repositorioUsuario){
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public Usuario consultarUsuario (String email, String password) {
        return repositorioUsuario.buscarUsuario(email, password);
    }

    @Override
    public void registrar(Usuario usuario) throws UsuarioExistente {
        Usuario usuarioEncontrado = repositorioUsuario.buscarUsuario(usuario.getEmail(), usuario.getPassword());
        if(usuarioEncontrado != null){
            throw new UsuarioExistente();
        }
        repositorioUsuario.guardar(usuario);
    }
    @Override
    public Usuario buscarUsuario(Long idUsuario) {
        return repositorioUsuario.buscarUsuarioPorId(idUsuario);
    }

}

