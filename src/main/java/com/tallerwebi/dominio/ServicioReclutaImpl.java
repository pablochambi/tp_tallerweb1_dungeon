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

//    private final List<UsuarioSemana> usuarioSemanas;
//    private final List<UsuarioHeroe> usuarioHeroes;
    private final RepositorioCarruaje repositorioCarruaje;
    private final RepositorioHeroe repositorioHeroe;
    private final RepositorioUsuario repositorioUsuario;
    private final Repositorio_carruajeHeroe repositorio_carruajeHeroe;
    private final Repositorio_usuarioHeroe repositorio_usuarioHeroe;
    private final List<Heroe> listaHeroesExistentes;

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

        //Hice comentario en ServicioReclutaImpl

        Heroe h1 = this.repositorioHeroe.guardar(new Heroe(1L, "Cruzado", 300, "/imagenes/cruzado.webp"));
        Heroe h2 = this.repositorioHeroe.guardar(new Heroe(2L, "Vestal", 200, "/imagenes/Vestal.webp"));

        this.listaHeroesExistentes = List.of(h1,h2);

    }

    @Override
    public Usuario registrarUnUsuario(Usuario usuario) {
        repositorioUsuario.guardar(usuario);
        return repositorioUsuario.buscarUsuarioPorId(usuario.getId());
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
            Carruaje carruaje = repositorioCarruaje.guardar(new Carruaje(0,0,0));
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





//
//    @Override
//    public CarruajeHeroe reclutarSegunIdHeroe(Long idHeroe) {
//        var iterator = carruajeHeroes.iterator();
//        while (iterator.hasNext()) {
//            CarruajeHeroe carruajeHeroe = iterator.next();
//            if (carruajeHeroe.getHeroe().getId().equals(idHeroe)) {
//                iterator.remove(); // Eliminación segura del iterador
//                // Actualizar usuarioHeroes o cualquier otra estructura relevante
//                Usuario usuario = carruajeHeroe.getCarruaje().getUsuario();
//                if (usuario != null) {
//                    usuarioHeroes.add(new UsuarioHeroe(usuario, carruajeHeroe.getHeroe()));
//                }
//                return carruajeHeroe;
//            }
//        }
//        return null;
//    }




//    @Override
//    public void setHeroeEnCarruaje(Heroe heroe, Carruaje carruaje) {
//        CarruajeHeroe ch = new CarruajeHeroe(carruaje,heroe);
//        carruajeHeroes.add(ch);
//    }
//
//    @Override
//    public Integer getCantidadDeHeroesDisponiblesEnCarruaje(Carruaje carruaje) {
//        Integer cantidad = 0;
//        for(CarruajeHeroe carruajeHeroe : carruajeHeroes){
//            if(carruajeHeroe.getCarruaje().getId().equals(carruaje.getId())){
//                cantidad++;
//            }
//        }
//        return cantidad;
//    }
//
//    @Override
//    public void mostrarListaDeHeroesEnCarruaje(Carruaje carruaje) {
//        Carruaje c = carruaje;
//        System.out.printf("Héroes disponibles en carruaje: ID: %d, Nivel: %d, Semana: %d\n",c.getId(),c.getNivel(),c.getSemana());
//        for (CarruajeHeroe ch : this.carruajeHeroes) {
//            if(ch.getCarruaje().getId().equals(c.getId())){
//                Heroe h = ch.getHeroe();
//                System.out.printf("ID: %d,   Nom: %s,   Niv: %d\n", h.getId(),h.getNombre(),h.getNivel());
//            }
//
//        }
//    }
//    @Override
//    public void mostrarListaDeHeroesDelUsuario(Carruaje carruaje) {
//        Carruaje c = carruaje;
//        System.out.printf("Héroes Que tiene el usuario: ID: %d,Email: %s\n",c.getUsuario().getId(),c.getUsuario().getEmail());
//        for (UsuarioHeroe uh : this.usuarioHeroes) {
//            if(uh.getUsuario().getId().equals(c.getUsuario().getId())){
//                Heroe h = uh.getHeroe();
//                System.out.printf("ID: %d,   Nom: %s,   Niv: %d\n", h.getId(),h.getNombre(),h.getNivel());
//            }
//
//        }
//        System.out.print("__________________________________________________________________________________\n");
//    }
//
//    @Override
//    public Integer getCantidadDeHeroesEnUsuarioHeroeSegunCarruaje(Carruaje carruaje) {
//        int cantidad = 0;
//        for (UsuarioHeroe usuarioHeroe : usuarioHeroes) {
//            if (usuarioHeroe.getUsuario().getId().equals(carruaje.getUsuario().getId())) {
//                cantidad++;
//            }
//        }
//        return cantidad;
//    }
//
//    @Override
//    public Carruaje obtenerCarruajePorUsuario(Usuario usuario) {
//        for (Carruaje carruaje : carruajes) {
//            if (carruaje.getUsuario().equals(usuario)) {
//                return carruaje;
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public Integer getCantidadDeHeroesEnUsuarioHeroeSegunUsuario(Usuario usuario) {
//        int cantidad = 0;
//        for (UsuarioHeroe usuarioHeroe : usuarioHeroes) {
//            if (usuarioHeroe.getUsuario().getId().equals(usuario.getId())) {
//                cantidad++;
//            }
//        }
//        return cantidad;
//    }



//    @Override
//    public Carruaje getAsignarCarruajeAUsuario(Usuario usuario) {
//
//        if(usuarios.contains(usuario)){
//            return buscarCarruajeAsignadoAlUsuario(usuario);
//        }
//        Carruaje carruaje;
//        carruaje = new Carruaje();
//        carruaje.setUsuario(usuario);
//
//        Heroe h1 = getHeroeSegunID(1L);
//        Heroe h2 = getHeroeSegunID(2L);
//
//        setHeroeEnCarruaje(h1,carruaje);
//        setHeroeEnCarruaje(h2,carruaje);
//
//        return carruaje ;
//
//    }

//    @Override
//    public Integer getNumeroDeSemanaSegunCarruaje(Carruaje carruaje) {
//        return carruaje.getSemana();
//    }
//
//    @Override
//    public Integer getNivelSegunCarruaje(Carruaje carruaje) {
//        return carruaje.getNivel();
//    }
//
//    @Override
//    public Integer getCantidadDeHeroesSemanalesSegunCarruaje(Carruaje carruaje) {
//        return carruaje.getCantidadDeHeroesSemanales();
//    }

//    @Override
//    public Heroe reclutarUnHeroeAleatorio(Carruaje carruaje) {
//
//        List<Heroe> heroesEnElCarruaje = obtenerListaDeHeroesEnElCarruaje(carruaje);
//        mesclarListaAleatoriamente(heroesEnElCarruaje);
//
//        Heroe heroe = heroesEnElCarruaje.get(0);
//        removerHeroeDelCarruaje(heroe,carruaje);
//        setUsuarioHeroes(carruaje.getUsuario(),heroe);
//
//        return heroe;
//    }
//
//    @Override
//    public void pasarSemana(Carruaje carruaje) {
//
//        carruaje.pasaUnaSemana();
//
//        setSemanaEnUsuarioSemanaSegunCarruaje(carruaje);
//
//        List<Heroe> heroesEnCamino = buscarHeroesQueNoEstanEnCarruajeNiEnUsuario();
//
//
//        int numeroDeConversiones = Math.min(getCantidadDeHeroesSemanalesSegunCarruaje(carruaje), heroesEnCamino.size());//Tira el minimo Entre esos dos valores
//
//        this.mesclarListaAleatoriamente(heroesEnCamino);
//
//        for (int i = 0; i < numeroDeConversiones; i++) {
//            Heroe heroe = heroesEnCamino.get(i);
//            this.setHeroeEnCarruaje(heroe, carruaje);
//        }
//
//    }
//
//    private List<Heroe> buscarHeroesQueNoEstanEnCarruajeNiEnUsuario() {
//        // Crear una lista para almacenar los héroes en camino
//        List<Heroe> heroesEnCamino = new ArrayList<>();
//
//        // Crear un conjunto para almacenar IDs de héroes en carruajes
//        Set<Long> heroesEnCarruajeIds = new HashSet<>();
//        for (CarruajeHeroe ch : carruajeHeroes) {
//            heroesEnCarruajeIds.add(ch.getHeroe().getId());
//        }
//
//        // Crear un conjunto para almacenar IDs de héroes en usuarios
//        Set<Long> heroesEnUsuarioIds = new HashSet<>();
//        for (UsuarioHeroe uh : usuarioHeroes) {
//            heroesEnUsuarioIds.add(uh.getHeroe().getId());
//        }
//
//        // Buscar héroes que no están ni en carruajes ni en usuarios
//        for (Heroe h : totalHeroes) {
//            if (!heroesEnCarruajeIds.contains(h.getId()) && !heroesEnUsuarioIds.contains(h.getId())) {
//                heroesEnCamino.add(h);
//            }
//        }
//
//        // Retornar la lista de héroes en camino
//        return heroesEnCamino;
//    }
//
//
//
//    @Override
//    public void setHeroe(Heroe heroe) {
//        this.totalHeroes.add(heroe);
//    }

//    @Override
//    public void aumentarNivel(Carruaje carruaje) {
//        carruaje.aumentarNivel();
//    }

//    @Override
//    public Heroe getHeroeSegunID(long id) {
//        return buscarHeroeSegunID(id);
//    }

//    @Override
//    public void agregarCarruaje(Carruaje carruaje) {
//        this.carruajes.add(carruaje);
//    }

//    @Override
//    public void setSemanaEnUsuarioSemanaSegunCarruaje(Carruaje carruaje) {
//
//        UsuarioSemana usBus = bucarUsuarioSemanaSegunCarruaje(carruaje);
//        if(usBus != null){
//            usBus.setSemana(carruaje.getSemana());
//            return;
//        }
//
//        UsuarioSemana us = new UsuarioSemana();
//        us.setUsuario(carruaje.getUsuario());
//        us.setSemana(carruaje.getSemana());
//        usuarioSemanas.add(us);
//    }



//    private UsuarioSemana bucarUsuarioSemanaSegunCarruaje(Carruaje carruaje) {
//        for(UsuarioSemana us : usuarioSemanas){
//            if(us.getUsuario().equals(carruaje.getUsuario())){
//                return us;
//            }
//        }
//        return null;
//    }
//
//    private Heroe buscarHeroeSegunID(long id) {
//        for(Heroe h : this.totalHeroes){
//            if(h.getId().equals(id)){
//                return h;
//            }
//        }
//        return null;
//    }


//    private void removerHeroeDelCarruaje(Heroe heroe, Carruaje carruaje) {
//
//        var iterator = carruajeHeroes.iterator(); // Creamos un iterador para la lista
//
//        while (iterator.hasNext()) {
//            CarruajeHeroe carruajeHeroe = iterator.next();
//            // Comprobamos las condiciones
//            if (carruajeHeroe.getCarruaje().getId().equals(carruaje.getId()) &&
//                    carruajeHeroe.getHeroe().getId().equals(heroe.getId())) {
//                iterator.remove(); // Eliminamos el elemento de manera segura
//            }
//        }
//
//
//    }
//
//    private void mesclarListaAleatoriamente(List<Heroe> heroesEnElCarruaje) {
//        Collections.shuffle(heroesEnElCarruaje);
//    }
//
//    private List<Heroe> obtenerListaDeHeroesEnElCarruaje(Carruaje carruaje) {
//        List<Heroe> heroesEnElCarruaje = new ArrayList<>();
//        for(CarruajeHeroe carruajeHeroe : carruajeHeroes){
//            if(carruajeHeroe.getCarruaje().getId().equals(carruaje.getId())){
//                heroesEnElCarruaje.add(carruajeHeroe.getHeroe());
//            }
//        }
//        return heroesEnElCarruaje;
//    }



//    private Carruaje buscarCarruajeAsignadoAlUsuario(Usuario usuario) {
//        for(Carruaje c: carruajes){
//            if(c.getUsuario().equals(usuario)){
//                return c;
//            }
//        }
//        return null;
//    }
    
//    public List<UsuarioSemana> getUsuarioSemanas() {
//        return usuarioSemanas;
//    }
//
//    public void setUsuarioSemanas() {
//        UsuarioSemana us = new UsuarioSemana();
//        us.setUsuario( new Usuario());
//        us.setSemana(0);
//
//        usuarioSemanas.add(new UsuarioSemana());
//    }

//    public List<UsuarioHeroe> getUsuarioHeroes() {
//        return usuarioHeroes;
//    }
//    public void setUsuarioHeroes(Usuario usuario, Heroe heroe) {
//        this.usuarioHeroes.add(new UsuarioHeroe(usuario,heroe));
//    }

}

