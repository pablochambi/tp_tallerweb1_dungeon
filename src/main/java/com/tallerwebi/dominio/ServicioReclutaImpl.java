package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidades.*;
import com.tallerwebi.dominio.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class ServicioReclutaImpl implements ServicioRecluta {

    private final RepositorioCarruaje repositorioCarruaje;
    private final RepositorioHeroe repositorioHeroe;
    private final RepositorioUsuario repositorioUsuario;
    private final Repositorio_carruajeHeroe repositorio_carruajeHeroe;
    private final Repositorio_usuarioHeroe repositorio_usuarioHeroe;

    @Autowired
    public ServicioReclutaImpl(RepositorioCarruaje repositorioCarruaje,
                               RepositorioHeroe repositorioHeroe,
                               RepositorioUsuario repositorioUsuario,
                               Repositorio_carruajeHeroe repositorio_carruajeHeroe,
                               Repositorio_usuarioHeroe repositorio_usuarioHeroe) {
        this.repositorioCarruaje = repositorioCarruaje;
        this.repositorioHeroe = repositorioHeroe;
        this.repositorioUsuario = repositorioUsuario;
        this.repositorio_carruajeHeroe = repositorio_carruajeHeroe;
        this.repositorio_usuarioHeroe = repositorio_usuarioHeroe;

    }

    @Override
    public Usuario registrarUnUsuario(Usuario usuario) {
        repositorioUsuario.guardar(usuario);
        return repositorioUsuario.buscarUsuarioPorId(usuario.getId());
    }

    @Override
    public Usuario getUsuarioRegistradoPorId(Long idUsuario) {
        return repositorioUsuario.buscarUsuarioPorId(idUsuario);
    }

    @Override
    public Carruaje getCarruajeDelUsuarioPorId(Long idUsuario) {
        Usuario usuario = repositorioUsuario.buscarUsuarioPorId(idUsuario);
        if (usuario == null) throw new RuntimeException("Usuario no encontrado");
        return repositorioCarruaje.buscarCarruajeAsignadoAUnUsuario(usuario);
    }

    @Override
    public Usuario getUsuarioPorId(Long idUsuario) {
        return repositorioUsuario.buscarUsuarioPorId(idUsuario);
    }

    @Override
    public Carruaje getCarruajeDelUsuarioId(Long idUsuario) {
        Usuario buscado = repositorioUsuario.buscarUsuarioPorId(idUsuario);
        if (buscado == null) throw new RuntimeException("Usuario no encontrado");
        return repositorioCarruaje.buscarCarruajeAsignadoAUnUsuario(buscado);
    }

    @Override
    public List<Heroe> getHeroesDisponiblesEnCarruaje(Carruaje carruaje) {

        Carruaje encontrado = repositorioCarruaje.buscarCarruajePorId(carruaje.getId());
        if (encontrado == null)  throw new RuntimeException("No se encontro el carruaje");

        List<Heroe> heroes = repositorio_carruajeHeroe.getListaDeHeroes(carruaje.getId());

        if(heroes == null) throw new RuntimeException("La lista de heroes esta nula");
        if(heroes.isEmpty()) throw new RuntimeException("No hay heroes en carruaje");

        return heroes;
    }

    @Override
    public void quitarUnHeroeDelCarruaje(Long idHeroe, Carruaje carruaje) {

        Heroe heroeBuscado =  repositorioHeroe.buscarHeroePorId(idHeroe);

        Carruaje carruajeBuscado = repositorioCarruaje.buscarCarruajePorId(idHeroe);

        if(heroeBuscado == null)  throw new RuntimeException("No se encontro heroe");

        if(carruajeBuscado == null) throw new RuntimeException("No se encontro carruaje");

        CarruajeHeroe carruajeHeroeBuscado  = repositorio_carruajeHeroe.buscarRelacion(carruajeBuscado,heroeBuscado);

        if(carruajeHeroeBuscado == null) throw new RuntimeException("No se encontro heroe en este carruaje");

        repositorio_carruajeHeroe.removerRelacion(carruajeHeroeBuscado);

    }

    @Override
    public void setRelacionEntreCarruajeYHeroe(Long idCarruaje, Long idHeroe) {

        Heroe heroeBuscado =  repositorioHeroe.buscarHeroePorId(idHeroe);
        Carruaje carruajeBuscado = repositorioCarruaje.buscarCarruajePorId(idCarruaje);

        if(heroeBuscado == null)  throw new RuntimeException("No se encontro heroe");
        if(carruajeBuscado == null) throw new RuntimeException("No se encontro carruaje");

        CarruajeHeroe carruajeHeroeBuscado  = repositorio_carruajeHeroe.buscarRelacion(carruajeBuscado,heroeBuscado);

        if(carruajeHeroeBuscado == null) {
            repositorio_carruajeHeroe.agregarRelacion(carruajeBuscado,heroeBuscado);
        }

        if(carruajeHeroeBuscado != null) throw new RuntimeException("Error, ya existe la relacion entre carruaje y heroe");

    }

    @Override
    public Carruaje asignarOActualizarUnCarrujeAUnUsuario(Long idUsuario) {

        Usuario usuarioBuscado = repositorioUsuario.buscarUsuarioPorId(idUsuario);
        if(usuarioBuscado == null) throw new RuntimeException("No se encontro usuario");

        Carruaje carruajeBus = repositorioCarruaje.buscarCarruajeAsignadoAUnUsuario(usuarioBuscado);

        if(carruajeBus == null) {
            Carruaje carruaje = repositorioCarruaje.guardar(new Carruaje(0,0,2));

            if (carruaje == null) throw new RuntimeException("No se guardo carruaje");
            if (carruaje.getId() == null) throw new RuntimeException("El carruaje tiene id nulo");

            List<Heroe> listaHeroesExistentes = repositorioHeroe.getListaDeHeroes();

            if (listaHeroesExistentes.isEmpty()) throw new RuntimeException("La lista esta vacia o nula");

            repositorioCarruaje.asignarUsuarioAUnCarruje(carruaje,usuarioBuscado);
            repositorio_carruajeHeroe.agregarRelacion(carruaje,listaHeroesExistentes.get(0));
            repositorio_carruajeHeroe.agregarRelacion(carruaje,listaHeroesExistentes.get(1));
            return carruaje;
        }

        return carruajeBus;
    }



    @Override
    public void agregarUnHeroeAlUsuario(Long idHeroe, Usuario usuario) {

        Heroe heroeBuscado =  repositorioHeroe.buscarHeroePorId(idHeroe);
        Usuario usuarioBuscado = repositorioUsuario.buscarUsuarioPorId(usuario.getId());

        if(heroeBuscado == null)  throw new RuntimeException("No se encontro heroe");

        if(usuarioBuscado == null) throw new RuntimeException("No se encontro usuario");

        UsuarioHeroe usuarioHeroeBus  = repositorio_usuarioHeroe.buscarRelacion(usuarioBuscado,heroeBuscado);

        if(usuarioHeroeBus == null) {
            repositorio_usuarioHeroe.agregarRelacion(usuarioBuscado,heroeBuscado);
        }
        if(usuarioHeroeBus != null) throw new RuntimeException("Este heroe ya existe en el usuario");
    }

    @Override
    public List<Heroe> getHeroesDisponiblesEnUsuario(Long idUsuario) {

        Usuario usuarioBus = repositorioUsuario.buscarUsuarioPorId(idUsuario);

        if(usuarioBus == null) throw new RuntimeException("No se encontro el Usuario");

        List<Heroe> listaDeHeroes = repositorio_usuarioHeroe.getListaDeHeroes(idUsuario);

        if(listaDeHeroes == null) throw new RuntimeException("La lista es nula");
        if(listaDeHeroes.isEmpty()) throw new RuntimeException("La lista no tiene heroes");

        return listaDeHeroes;
    }


    @Override
    public List<Heroe> getListaDeHeroesEnCarruaje(Carruaje carruaje) {
        Carruaje carruajeBus = repositorioCarruaje.buscarCarruajePorId(carruaje.getId());

        if(carruajeBus == null) throw new RuntimeException("No se encontro el carruaje");

        List<Heroe> listaDeHeroes = repositorio_carruajeHeroe.getListaDeHeroes(carruaje.getId());

        return listaDeHeroes;
    }


    @Override
    public List<Heroe> getHeroesObtenidosPorElUsuario(Usuario usuario) {

        Usuario usuarioBuscado = repositorioUsuario.buscarUsuarioPorId(usuario.getId());

        if(usuarioBuscado == null)  throw new RuntimeException("No se encontro el usuario");

        List<Heroe> listaDeHeroes = repositorio_usuarioHeroe.getListaDeHeroes(usuarioBuscado.getId());

        return listaDeHeroes;
    }



}

