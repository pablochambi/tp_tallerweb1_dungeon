package com.tallerwebi.dominio;
import com.tallerwebi.dominio.entidades.*;
import com.tallerwebi.dominio.interfaces.*;
import com.tallerwebi.infraestructura.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


public class ServicioReclutaTest {

    private ServicioRecluta servicioReclutas;
    private RepositorioHeroe repositorioHeroe;
    private RepositorioCarruaje repositorioCarruaje;
    private RepositorioUsuario repositorioUsuario;
    private Repositorio_carruajeHeroe repositorio_carruajeHeroe;
    private Repositorio_usuarioHeroe repositorio_usuarioHeroe;

    private Usuario usuarioMock;
    private Carruaje carruajeMock;
    private Heroe heroe1;
    private Heroe heroe2;
    private List<Heroe> listaDeHeroesMock;
    private CarruajeHeroe carruajeHeroe1Mock;
    private UsuarioHeroe usuarioHeroe1Mock;

    @BeforeEach
    public void init() {

        repositorioCarruaje = Mockito.mock(RepositorioCarruajeImpl.class);
        repositorioHeroe = Mockito.mock(RepositorioHeroeImpl.class);
        repositorioUsuario = Mockito.mock(RepositorioUsuarioImpl.class);
        repositorio_carruajeHeroe = Mockito.mock(Repositorio_carruajeHeroeImpl.class);
        repositorio_usuarioHeroe = Mockito.mock(Repositorio_usuarioHeroeImpl.class);

        // Configuración inicial
        usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        usuarioMock.setEmail("admin@admin.com");

        carruajeMock = new Carruaje();
        carruajeMock.setId(1L);
        carruajeMock.setNivel(0);
        carruajeMock.setSemana(0);
        carruajeMock.setCantidadDeHeroesSemanales(2);
        carruajeMock.setUsuario(usuarioMock);

        carruajeHeroe1Mock = new CarruajeHeroe();
        carruajeHeroe1Mock.setId(1L);
        carruajeHeroe1Mock.setHeroe(heroe1);
        carruajeHeroe1Mock.setCarruaje(carruajeMock);

        usuarioHeroe1Mock = new UsuarioHeroe();
        usuarioHeroe1Mock.setId(1L);
        usuarioHeroe1Mock.setHeroe(heroe1);
        usuarioHeroe1Mock.setUsuario(usuarioMock);

        servicioReclutas = new ServicioReclutaImpl(
                repositorioCarruaje,repositorioHeroe,repositorioUsuario,
                repositorio_carruajeHeroe,repositorio_usuarioHeroe);

        heroe1 = new Heroe(1L, "Cruzado", 300, "/imagenes/cruzado.webp");
        heroe2 = new Heroe(2L, "Vestal", 200, "/imagenes/Vestal.webp");
        listaDeHeroesMock = List.of(heroe1, heroe2);
    }

    //Me quede en hacer mas test de carruaje y si no hay mas entonces hacer el siguiente
    //Ya hice dos test en repositorioCarruajeTest

    @Test
    public void queSePuedaAsignarUnCarruaje_AUnUsuarioNuevo() {

        // Configuración
        when(repositorioUsuario.buscarUsuarioPorId(usuarioMock.getId())).thenReturn(usuarioMock);
        when(repositorioCarruaje.buscarCarruajeAsignadoAUnUsuario(usuarioMock)).thenReturn(null);

        // Ejecución
        Carruaje carruaje  = servicioReclutas.asignarOActualizarUnCarrujeAUnUsuario(usuarioMock.getId());

        // Verificación
        assertThat(carruaje,notNullValue());
        assertThat(carruaje.getNivel(),equalTo(0));
        verify(repositorioCarruaje).guardar(any(Carruaje.class));
        verify(repositorioCarruaje).asignarUsuarioAUnCarruje(any(Carruaje.class),any(Usuario.class));
    }

    @Test
    public void queSePuedaAsignarUnCarruajeCon2Heroes_AUnUsuarioNuevo() {

        // Configuración
        when(repositorioUsuario.buscarUsuarioPorId(usuarioMock.getId())).thenReturn(usuarioMock);
        when(repositorioCarruaje.buscarCarruajePorId(carruajeMock.getId())).thenReturn(carruajeMock);

        when(repositorioCarruaje.buscarCarruajeAsignadoAUnUsuario(usuarioMock)).thenReturn(null);
        when(repositorioCarruaje.guardar(any(Carruaje.class))).thenReturn(carruajeMock);
        when(repositorio_carruajeHeroe.getListaDeHeroes(carruajeMock.getId())).thenReturn(listaDeHeroesMock);

        // Ejecución
        Carruaje carruaje  = servicioReclutas.asignarOActualizarUnCarrujeAUnUsuario(usuarioMock.getId());
        List<Heroe> listaObtenida = servicioReclutas.getHeroesDisponiblesEnCarruaje(carruaje);

        // Verificación
        assertThat(carruaje,notNullValue());
        assertThat(carruaje.getId(),equalTo(1L));
        assertThat(carruaje.getNivel(),equalTo(0));
        assertThat(listaObtenida.size(),equalTo(2));

        verify(repositorioCarruaje).guardar(any(Carruaje.class));
        verify(repositorioCarruaje).asignarUsuarioAUnCarruje(any(Carruaje.class),any(Usuario.class));
    }

    @Test
    public void queNoSePuedaAsignarUnCarruaje_AUnUsuarioQueYaTieneCarruaje() {

        // Configuración
        when(repositorioUsuario.buscarUsuarioPorId(usuarioMock.getId())).thenReturn(usuarioMock);
        when(repositorioCarruaje.buscarCarruajeAsignadoAUnUsuario(usuarioMock)).thenReturn(carruajeMock);

        // Ejecución
        servicioReclutas.asignarOActualizarUnCarrujeAUnUsuario(usuarioMock.getId());

        // Verificación
        verify(repositorioCarruaje,never()).guardar(any(Carruaje.class));
        verify(repositorioCarruaje,never()).asignarUsuarioAUnCarruje(any(Carruaje.class),any(Usuario.class));
    }

    @Test
    public void queSePuedaAgregarLaRelacionEntreUnCarruajeYUnHeroe() {
        // Configuración
        List<Heroe> heroesEnCarruaje = List.of(heroe1,heroe2);

        when(repositorioHeroe.buscarHeroePorId(heroe1.getId())).thenReturn(heroe1);
        when(repositorioCarruaje.buscarCarruajePorId(carruajeMock.getId())).thenReturn(carruajeMock);
        when(repositorio_carruajeHeroe.buscarRelacion(carruajeMock,heroe1)).thenReturn(null);

        // Ejecución
        servicioReclutas.setRelacionEntreCarruajeYHeroe(carruajeMock.getId(),heroe1.getId());

        // Verificación
        verify(repositorio_carruajeHeroe).buscarRelacion(carruajeMock,heroe1);
    }




    // -------------------- Tests para getHeroesDisponiblesEnCarruaje --------------------
    @Test
    public void queSePuedanObtenerLosHeroesDisponiblesEnElCarruaje() {
        // Configuración
        List<Heroe> heroesEnCarruaje = List.of(heroe1,heroe2);

        when(repositorioCarruaje.buscarCarruajePorId(carruajeMock.getId())).thenReturn(carruajeMock);
        when(repositorio_carruajeHeroe.getListaDeHeroes(carruajeMock.getId())).thenReturn(heroesEnCarruaje);

        // Ejecución
        List<Heroe> resultado = servicioReclutas.getHeroesDisponiblesEnCarruaje(carruajeMock);

        // Verificación
        assertThat(resultado, hasSize(2));
        assertThat(resultado, containsInAnyOrder(heroe1, heroe2));
    }

    @Test
    public void queSePuedanObtenerLosHeroesDisponiblesEnElUsuario() {
        // Configuración
        List<Heroe> heroesEnCarruaje = List.of(heroe1,heroe2);

        when(repositorioUsuario.buscarUsuarioPorId(usuarioMock.getId())).thenReturn(usuarioMock);
        when(repositorio_usuarioHeroe.getListaDeHeroes(usuarioMock.getId())).thenReturn(heroesEnCarruaje);

        // Ejecución
        List<Heroe> resultado = servicioReclutas.getHeroesDisponiblesEnUsuario(usuarioMock.getId());

        // Verificación
        assertThat(resultado.size(), equalTo(2));
        assertThat(resultado, containsInAnyOrder(heroe1, heroe2));
    }



    @Test
    public void queDevuelvaUnaExcepcion_SiNoHayHeroesEnElCarruaje() {
        // Configuración
        List<Heroe> heroesEnCarruaje = List.of();
        when(repositorioCarruaje.buscarCarruajePorId(carruajeMock.getId())).thenReturn(carruajeMock);
        when(repositorio_carruajeHeroe.getListaDeHeroes(carruajeMock.getId())).thenReturn(heroesEnCarruaje);

        // Ejecución y Verificación
        Exception exception = assertThrows(RuntimeException.class, () -> {
                                servicioReclutas.getHeroesDisponiblesEnCarruaje(carruajeMock);
                            });
        // Verificación
        assertThat(exception.getMessage(),equalTo("No hay heroes en carruaje"));
    }
    // -------------------- Tests para quitarUnHeroeDelCarruaje --------------------
    @Test
    public void queSePuedaQuitarUnHeroeDelCarruaje() {

        //Preparacion
        when(repositorioHeroe.buscarHeroePorId(heroe1.getId())).thenReturn(heroe1);
        when(repositorioCarruaje.buscarCarruajePorId(carruajeMock.getId())).thenReturn(carruajeMock);
        when(repositorio_carruajeHeroe.buscarRelacion(carruajeMock,heroe1)).thenReturn(carruajeHeroe1Mock);

        //Ejecucion
        servicioReclutas.quitarUnHeroeDelCarruaje(heroe1.getId(),carruajeMock);

        //Verificacion
        verify(repositorio_carruajeHeroe).removerRelacion(carruajeHeroe1Mock);
    }
    @Test
    public void queLanceUnaExcepcion_AlQuitarHeroeInexistenteDelCarruaje() {
        // Configuración

        when(repositorioHeroe.buscarHeroePorId(heroe1.getId())).thenReturn(heroe1);
        when(repositorioCarruaje.buscarCarruajePorId(carruajeMock.getId())).thenReturn(carruajeMock);

        when(repositorio_carruajeHeroe.buscarRelacion(carruajeMock,heroe1)).thenReturn(null);

        // Ejecución y Verificación
        Exception e = assertThrows(RuntimeException.class, () -> {
            servicioReclutas.quitarUnHeroeDelCarruaje(heroe1.getId(), carruajeMock);
        });

        // Verificar que no se modificó el carruaje
        assertThat(e.getMessage(),equalTo("No se encontro heroe en este carruaje"));
        verify(repositorio_carruajeHeroe,never()).removerRelacion(carruajeHeroe1Mock);
    }

    // -------------------- Tests para agregarUnHeroeAlUsuario --------------------
    @Test
    public void queSePuedaAgregarUnHeroeAlUsuario() {
        // Configuración

        when(repositorioHeroe.buscarHeroePorId(heroe1.getId())).thenReturn(heroe1);
        when(repositorioUsuario.buscarUsuarioPorId(usuarioMock.getId())).thenReturn(usuarioMock);
        when(repositorio_usuarioHeroe.buscarRelacion(usuarioMock,heroe1)).thenReturn(null);
        when(repositorio_usuarioHeroe.getListaDeHeroes(usuarioMock.getId())).thenReturn(List.of(heroe1));

        // Ejecución

        servicioReclutas.agregarUnHeroeAlUsuario(heroe1.getId(), usuarioMock);

        // Verificación
        verify(repositorio_usuarioHeroe).agregarRelacion(usuarioMock,heroe1);
        assertThat(servicioReclutas.getHeroesObtenidosPorElUsuario(usuarioMock), instanceOf(List.class));
        assertThat(servicioReclutas.getHeroesObtenidosPorElUsuario(usuarioMock).size(),equalTo(1));
    }

    @Test
    public void queLanceUnaExcepcion_AlAgregarHeroeQueYaTieneElUsuario() {

        // Configuración
        when(repositorioHeroe.buscarHeroePorId(heroe1.getId())).thenReturn(heroe1);
        when(repositorioUsuario.buscarUsuarioPorId(usuarioMock.getId())).thenReturn(usuarioMock);
        when(repositorio_usuarioHeroe.buscarRelacion(usuarioMock, heroe1)).thenReturn(usuarioHeroe1Mock);

        // Ejecución y Verificación
        Exception e = assertThrows(RuntimeException.class, () -> {
            servicioReclutas.agregarUnHeroeAlUsuario(heroe1.getId(), usuarioMock);
        });

        // Verificar
        assertThat(e.getMessage(), equalTo("Este heroe ya existe en el usuario"));
        verify(repositorio_usuarioHeroe, never()).agregarRelacion(usuarioMock, heroe1);
    }

    //
//    // -------------------- Tests para reclutarHeroeCompleto --------------------
    @Test
    public void queSePuedaRecluatarUnHeroeDelCarruaje() {
        // Configuración

        when(repositorioHeroe.buscarHeroePorId(heroe1.getId())).thenReturn(heroe1);
        when(repositorioUsuario.buscarUsuarioPorId(usuarioMock.getId())).thenReturn(usuarioMock);

        when(repositorioCarruaje.buscarCarruajePorId(carruajeMock.getId())).thenReturn(carruajeMock);

        when(repositorio_carruajeHeroe.buscarRelacion(carruajeMock,heroe1)).thenReturn(carruajeHeroe1Mock);
        when(repositorio_usuarioHeroe.buscarRelacion(usuarioMock,heroe1)).thenReturn(null);

        when(repositorio_carruajeHeroe.getListaDeHeroes(carruajeMock.getId())).thenReturn(List.of(heroe1));

        // Ejecución
        servicioReclutas.quitarUnHeroeDelCarruaje(heroe1.getId(), carruajeMock);
        servicioReclutas.agregarUnHeroeAlUsuario(heroe1.getId(),usuarioMock);

        //Verificacion
        verify(repositorio_carruajeHeroe).removerRelacion(carruajeHeroe1Mock);
        verify(repositorio_usuarioHeroe).agregarRelacion(usuarioMock,heroe1);
        assertThat(servicioReclutas.getListaDeHeroesEnCarruaje(carruajeMock), instanceOf(List.class));
        assertThat(servicioReclutas.getListaDeHeroesEnCarruaje(carruajeMock).size(),equalTo(1));
    }


    // ─────────────────────────────────────────────────────────────────────────────
// Tests de nivel del carruaje y héroes semanales
// ─────────────────────────────────────────────────────────────────────────────

    @Test
    public void dadoNivel0_cuandoConsultoHeroesSemanales_entoncesDevuelve2() {
        Carruaje carruaje = new Carruaje();

        assertThat(carruaje.getNivel(), is(0));
        assertThat(carruaje.getCantidadDeHeroesSemanales(), is(2));
    }

    @Test
    public void dadoNivel1_cuandoConsultoHeroesSemanales_entoncesDevuelve3() {
        Carruaje carruaje = new Carruaje();
        carruaje.aumentarNivel(); // nivel = 1

        assertThat(carruaje.getNivel(), is(1));
        assertThat(carruaje.getCantidadDeHeroesSemanales(), is(3));
    }

    @Test
    public void dadoNivel2_cuandoConsultoHeroesSemanales_entoncesDevuelve4() {
        Carruaje carruaje = new Carruaje();
        carruaje.aumentarNivel(); // 1
        carruaje.aumentarNivel(); // 2

        assertThat(carruaje.getNivel(), is(2));
        assertThat(carruaje.getCantidadDeHeroesSemanales(), is(4));
    }

    @Test
    public void dadoNivel3_cuandoConsultoHeroesSemanales_entoncesDevuelve5() {
        Carruaje carruaje = new Carruaje();
        carruaje.aumentarNivel(); // 1
        carruaje.aumentarNivel(); // 2
        carruaje.aumentarNivel(); // 3

        assertThat(carruaje.getNivel(), is(3));
        assertThat(carruaje.getCantidadDeHeroesSemanales(), is(5));
    }

// ─────────────────────────────────────────────────────────────────────────────
// Tests de semana 0: reclutamiento y estado inicial
// ─────────────────────────────────────────────────────────────────────────────

//    @Test
//    public void cuandoSeInicializaCarruajeEnSemana0YNivel0_entoncesHay2HeroesDisponibles() {
//
//        Carruaje carruaje = givenExisteCarruajeConUsuarioAsignadoYDosHeroes();
//
//        //Compruebo datos iniciales del Carruaje
//        assertThat(servicioReclutas.getNumeroDeSemanaSegunCarruaje(carruaje), is(0));
//        assertThat(servicioReclutas.getNivelSegunCarruaje(carruaje), is(0));
//        assertThat(servicioReclutas.getCantidadDeHeroesDisponiblesEnCarruaje(carruaje), is(2));
//        assertThat(servicioReclutas.getCantidadDeHeroesSemanalesSegunCarruaje(carruaje), is(2));
//    }
//
//
//
//    @Test
//    public void dadoSemana0YNivel0_yReclutoUnHeroe_cuandoConsultoHeroesDisponibles_entoncesDevuelve1() {
//        Carruaje carruaje = givenExisteCarruajeConUsuarioAsignadoYDosHeroes();
//
//        Heroe heroeReclutado = servicioReclutas.reclutarUnHeroeAleatorio(carruaje);
//
//        heroeReclutado.mostrarHeroe();
//
//        assertThat(servicioReclutas.getCantidadDeHeroesDisponiblesEnCarruaje(carruaje), is(1));
//    }
//
//    @Test
//    public void dadoCarruajeConDosHeroes_whenReclutoUnHeroe_entoncesQuedaUnHeroeAsignadoYUnHeroeDisponible() {
//        Carruaje carruaje = givenExisteCarruajeConUsuarioAsignadoYDosHeroes();
//
//        Heroe heroeReclutado = servicioReclutas.reclutarUnHeroeAleatorio(carruaje);
//        heroeReclutado.mostrarHeroe();
//
//        assertThat(servicioReclutas.getCantidadDeHeroesEnUsuarioHeroeSegunCarruaje(carruaje), is(1));
//        assertThat(servicioReclutas.getCantidadDeHeroesEnUsuarioHeroeSegunUsuario(carruaje.getUsuario()), is(1));
//        assertThat(servicioReclutas.getCantidadDeHeroesDisponiblesEnCarruaje(carruaje), is(1));
//    }
//
//    @Test
//    public void dadoCarruajeConDosHeroes_whenReclutoDosHeroes_entoncesObtengoDosHeroesAsignadosYningunoDisponible() {
//        Carruaje carruaje = givenExisteCarruajeConUsuarioAsignadoYDosHeroes();
//
//        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);
//        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);
//
//        assertThat(servicioReclutas.getCantidadDeHeroesEnUsuarioHeroeSegunCarruaje(carruaje), is(2));
//        assertThat(servicioReclutas.getCantidadDeHeroesEnUsuarioHeroeSegunUsuario(carruaje.getUsuario()), is(2));
//        assertThat(servicioReclutas.getCantidadDeHeroesDisponiblesEnCarruaje(carruaje), is(0));
//    }
//
//
//    @Test
//    public void dadoSemana0YNivel0_yReclutoDosHeroes_cuandoConsultoHeroesDisponibles_entoncesDevuelve0() {
//        Carruaje carruaje = givenExisteCarruajeConUsuarioAsignadoYDosHeroes();
//
//        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);
//        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);
//
//        assertThat(servicioReclutas.getCantidadDeHeroesDisponiblesEnCarruaje(carruaje), is(0));
//    }
//
//    @Test
//    public void siReclutoUnHeroeSegunID__CarruajeDevuelve1HeroeDisponible() {
//        Carruaje carruaje = givenExisteCarruajeConUsuarioAsignadoYDosHeroes();
//
//        servicioReclutas.reclutarSegunIdHeroe(1L);
//
//        assertThat(servicioReclutas.getCantidadDeHeroesEnUsuarioHeroeSegunCarruaje(carruaje),is(1));
//        assertThat(servicioReclutas.getCantidadDeHeroesDisponiblesEnCarruaje(carruaje), is(1));
//    }
//
//    @Test
//    public void siRecluto2HeroesSegunID__CarruajeDevuelve0HeroeDisponible() {
//        Carruaje carruaje = givenExisteCarruajeConUsuarioAsignadoYDosHeroes();
//
//        servicioReclutas.reclutarSegunIdHeroe(1L);
//        servicioReclutas.reclutarSegunIdHeroe(2L);
//
//        assertThat(servicioReclutas.getCantidadDeHeroesEnUsuarioHeroeSegunCarruaje(carruaje),is(2));
//        assertThat(servicioReclutas.getCantidadDeHeroesDisponiblesEnCarruaje(carruaje), is(0));
//    }
//
//
//// ─────────────────────────────────────────────────────────────────────────────
//// Tests de paso de semana y recálculo de héroes disponibles
//// ─────────────────────────────────────────────────────────────────────────────
//
//    @Test
//    public void dadoSemana0_conDosReclutamientos_cuandoAvanzaASemana1_entoncesRestablece2HeroesDisponibles() {
//
//        Carruaje carruaje = givenExisteCarruajeConUsuarioAsignadoYDosHeroes();
//
//        // Semana 0 - reclutar 2 héroes
//        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);
//        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);
//
//        // Avanza a semana 1
//        servicioReclutas.pasarSemana(carruaje);
//
//        assertThat(servicioReclutas.getCantidadDeHeroesDisponiblesEnCarruaje(carruaje), is(2));
//    }
//
//
//
//    @Test
//    public void dadoSemana0_conUnReclutamiento_cuandoAvanzaASemana1_entoncesDevuelve3HeroesDisponibles() {
//        // dado Semana 0 con nivel 0 y 2 héroes iniciales
//        Carruaje carruaje = givenExisteCarruajeConUsuarioAsignadoYDosHeroes();
//
//        // recluta 1 héroe en semana 0
//        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);
//
//        // avanza a semana 1
//        servicioReclutas.pasarSemana(carruaje);
//
//        assertThat(servicioReclutas.getCantidadDeHeroesDisponiblesEnCarruaje(carruaje), is(3));
//    }
//
//
//    @Test
//    public void dadoSemana0_conUnReclutamientoYLuegoOtroEnSemana1_cuandoConsultoHeroesDisponibles_entoncesDevuelve2() {
//        // dado Semana 0 con nivel 0 y 2 héroes iniciales
//        Carruaje carruaje = givenExisteCarruajeConUsuarioAsignadoYDosHeroes();
//
//        // Semana 0: recluta 1 héroe
//        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);
//
//        // Avanza a semana 1
//        servicioReclutas.pasarSemana(carruaje);
//
//        // Semana 1: recluta otro héroe
//        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);
//
//        // Ahora debieran quedar 2 disponibles (1 remanente + 1 reclutado)
//        assertThat(servicioReclutas.getCantidadDeHeroesDisponiblesEnCarruaje(carruaje), is(2));
//    }
//
//    @Test
//    public void dadoCarruajeNivel1_enSemana1_llegan3HeroesYQuedaUnReclutado_entoncesQuedan4Disponibles() {
//        // dado Semana 0 con nivel 0 y 2 héroes iniciales
//        Carruaje carruaje = givenExisteCarruajeConUsuarioAsignadoYDosHeroes();
//
//        // Semana 0: recluta 1 héroe
//        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);
//
//        // Aumenta el nivel a 1 (3 héroes semanales a partir de la siguiente semana)
//        servicioReclutas.aumentarNivel(carruaje);
//
//        // Avanza a semana 1: llegan 3 nuevos héroes
//        servicioReclutas.pasarSemana(carruaje);
//
//        // Total disponibles = 1 remanente + 3 nuevos = 4
//        assertThat(servicioReclutas.getCantidadDeHeroesDisponiblesEnCarruaje(carruaje), is(4));
//        assertThat(servicioReclutas.getCantidadDeHeroesSemanalesSegunCarruaje(carruaje), is(3));
//    }
//
//    @Test
//    public void dadoCarruajeNivel2_enSemana1_llegan4HeroesYUnoFueReclutado_entoncesQuedan4Disponibles() {
//        // dado Semana 0 con nivel 0 y 2 héroes iniciales
//        Carruaje carruaje = givenExisteCarruajeConUsuarioAsignadoYDosHeroes();
//
//        // Semana 0: recluta 1 héroe
//        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);
//
//        // Aumenta el nivel a 1 (3 héroes semanales) y avanza a semana 1
//        servicioReclutas.aumentarNivel(carruaje);
//        servicioReclutas.pasarSemana(carruaje);
//
//        // Ahora aumenta el nivel a 2 (4 héroes semanales, que se aplicarán en la próxima semana)
//        servicioReclutas.aumentarNivel(carruaje);
//
//        // Sin avanzar otra semana, la disponibilidad sigue siendo la que quedó en S1
//        assertThat(servicioReclutas.getCantidadDeHeroesDisponiblesEnCarruaje(carruaje), is(4));
//        assertThat(servicioReclutas.getCantidadDeHeroesSemanalesSegunCarruaje(carruaje), is(4));
//    }
//
//    @Test
//    public void dadoCarruajeNivel2_enSemana2_llegan8HeroesYUnoFueReclutado_entoncesQuedan8Disponibles() {
//        // dado Semana 0 con nivel 0 y 2 héroes iniciales
//        Carruaje carruaje = givenExisteCarruajeConUsuarioAsignadoYDosHeroes();
//
//        // Semana 0: recluta 1 héroe
//        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);
//
//        // Nivel → 1 (herosSemanales = 3) y semana 1
//        servicioReclutas.aumentarNivel(carruaje);
//        servicioReclutas.pasarSemana(carruaje);
//
//        // Nivel → 2 (herosSemanales = 4) y semana 2
//        servicioReclutas.aumentarNivel(carruaje);
//        servicioReclutas.pasarSemana(carruaje);
//
//        //carruaje.mostrarTotalHeroes();
//
//        // En S2: 1 remanente + 3 nuevos de S1 + 4 nuevos de S2 = 8
//        assertThat(servicioReclutas.getCantidadDeHeroesDisponiblesEnCarruaje(carruaje), is(8));
//        assertThat(servicioReclutas.getCantidadDeHeroesSemanalesSegunCarruaje(carruaje), is(4));
//    }
//
//    @Test
//    public void dadoCarruajeNivel2_conReclutamientosEnSemana0Y2_cuandoConsultoHeroesDisponibles_entoncesDevuelve5() {
//        Carruaje carruaje = givenExisteCarruajeConUsuarioAsignadoYDosHeroes();
//
//        // Semana 0: recluta 1 héroe
//        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);//1hd
//
//        // Nivel → 1 y pasa a semana 1
//        servicioReclutas.aumentarNivel(carruaje);
//        servicioReclutas.pasarSemana(carruaje);//4hd
//
//        // Nivel → 2 y pasa a semana 2
//        servicioReclutas.aumentarNivel(carruaje);
//        servicioReclutas.pasarSemana(carruaje);//8hd
//
//        // En S2: recluta 3 héroes más
//        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);
//        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);
//        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);
//
//        // Disponibles = 1 remanente + 3 (S1) + 4 (S2) – 3 reclutados = 5
//        assertThat(servicioReclutas.getCantidadDeHeroesDisponiblesEnCarruaje(carruaje), is(5));
//        assertThat(servicioReclutas.getCantidadDeHeroesSemanalesSegunCarruaje(carruaje), is(4));
//    }
//
//    @Test
//    public void dadoCarruajeNivel2_conReclutamientosEnSemana0Y1_cuandoConsultoHeroesDisponibles_entoncesDevuelve3() {
//        // Inicializar carruaje con 2 héroes en S0
//        Carruaje carruaje = givenExisteCarruajeConUsuarioAsignadoYDosHeroes();
//
//        // Semana 0: reclutar 1 héroe
//        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);
//        // Subir directamente a nivel 2 (herosSemanales = 4)
//        servicioReclutas.aumentarNivel(carruaje);
//        servicioReclutas.aumentarNivel(carruaje);
//
//        // Avanzar a semana 1: llegan 4 héroes
//        servicioReclutas.pasarSemana(carruaje);
//        // Semana 1: reclutar 2 héroes
//        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);
//        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);
//
//        // Disponibles = 1 remanente + 4 nuevos - 2 reclutados = 3
//        assertThat(servicioReclutas.getCantidadDeHeroesDisponiblesEnCarruaje(carruaje), is(3));
//        assertThat(servicioReclutas.getCantidadDeHeroesSemanalesSegunCarruaje(carruaje), is(4));
//
//    }
//
//    @Test
//    public void __dadoCarruajeNivel2_conReclutamientosEnSemana0Y1_cuandoConsultoHeroesDisponibles_entoncesDevuelve3() {
//        // Inicializar carruaje con 2 héroes en S0
//        Carruaje carruaje = givenExisteCarruajeConUsuarioAsignadoYDosHeroes();
//
//        // Semana 0: reclutar 1 héroe
//        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);
//        // Subir directamente a nivel 2 (herosSemanales = 4)
//        servicioReclutas.aumentarNivel(carruaje);
//        servicioReclutas.aumentarNivel(carruaje);
//
//        // Avanzar a semana 1: llegan 4 héroes
//        servicioReclutas.pasarSemana(carruaje);
//        // Semana 1: reclutar 2 héroes
//        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);
//        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);
//
//        // Disponibles = 1 remanente + 4 nuevos - 2 reclutados = 3
//        assertThat(servicioReclutas.getCantidadDeHeroesDisponiblesEnCarruaje(carruaje), is(3));
//        assertThat(servicioReclutas.getCantidadDeHeroesSemanalesSegunCarruaje(carruaje), is(4));
//
//    }
//
//
//    private Carruaje givenExisteCarruajeConUsuarioAsignadoYDosHeroes() {
//
//        givenExiste19HeroesMasEnLaBaseDeDatos();
//
//        Usuario usuario = new Usuario();
//        usuario.setId(1L);
//        usuario.setEmail("jugador1");
//
//        Carruaje carruaje = servicioReclutas.getAsignarCarruajeAUsuario(usuario);//s0,n0
//        servicioReclutas.agregarCarruaje(carruaje);
//
//        servicioReclutas.setSemanaEnUsuarioSemanaSegunCarruaje(carruaje);
//
//
//        return carruaje;
//    }
//
//
//
//    private void givenExiste19HeroesMasEnLaBaseDeDatos() {
//
//        servicioReclutas.setHeroe(new Heroe(1L,"her1"));
//        servicioReclutas.setHeroe(new Heroe(2L,"her2"));
//        servicioReclutas.setHeroe(new Heroe(3L, "her3"));
//        servicioReclutas.setHeroe(new Heroe(4L, "her4"));
//        servicioReclutas.setHeroe(new Heroe(5L, "her5"));
//        servicioReclutas.setHeroe(new Heroe(6L, "her6"));
//        servicioReclutas.setHeroe(new Heroe(7L, "her7"));
//        servicioReclutas.setHeroe(new Heroe(8L, "her8"));
//        servicioReclutas.setHeroe(new Heroe(9L, "her9"));
//        servicioReclutas.setHeroe(new Heroe(10L, "her10"));
//        servicioReclutas.setHeroe(new Heroe(11L, "her11"));
//        servicioReclutas.setHeroe(new Heroe(12L, "her12"));
//        servicioReclutas.setHeroe(new Heroe(13L, "her13"));
//        servicioReclutas.setHeroe(new Heroe(14L, "her14"));
//        servicioReclutas.setHeroe(new Heroe(15L, "her15"));
//        servicioReclutas.setHeroe(new Heroe(16L, "her16"));
//        servicioReclutas.setHeroe(new Heroe(17L, "her17"));
//        servicioReclutas.setHeroe(new Heroe(18L, "her18"));
//        servicioReclutas.setHeroe(new Heroe(19L, "her19"));
//
//
//    }
//


}
