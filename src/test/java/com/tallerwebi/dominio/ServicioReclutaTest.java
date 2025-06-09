package com.tallerwebi.dominio;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class ServicioReclutaTest {

    ServicioRecluta servicioReclutas = new ServicioReclutaImpl();

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

    @Test
    public void cuandoSeInicializaCarruajeEnSemana0YNivel0_entoncesHay2HeroesDisponibles() {

        Carruaje carruaje = givenExisteCarruajeConUsuarioAsignadoYDosHeroes();

        //Compruebo datos iniciales del Carruaje
        assertThat(servicioReclutas.getNumeroDeSemanaSegunCarruaje(carruaje), is(0));
        assertThat(servicioReclutas.getNivelSegunCarruaje(carruaje), is(0));
        assertThat(servicioReclutas.getCantidadDeHeroesDisponiblesEnCarruaje(carruaje), is(2));
        assertThat(servicioReclutas.getCantidadDeHeroesSemanalesSegunCarruaje(carruaje), is(2));
    }

    

    @Test
    public void dadoSemana0YNivel0_yReclutoUnHeroe_cuandoConsultoHeroesDisponibles_entoncesDevuelve1() {
        Carruaje carruaje = givenExisteCarruajeConUsuarioAsignadoYDosHeroes();

        Heroe heroeReclutado = servicioReclutas.reclutarUnHeroeAleatorio(carruaje);

        heroeReclutado.mostrarHeroe();

        assertThat(servicioReclutas.getCantidadDeHeroesDisponiblesEnCarruaje(carruaje), is(1));
    }

    @Test
    public void dadoCarruajeConDosHeroes_whenReclutoUnHeroe_entoncesQuedaUnHeroeAsignadoYUnHeroeDisponible() {
        Carruaje carruaje = givenExisteCarruajeConUsuarioAsignadoYDosHeroes();

        Heroe heroeReclutado = servicioReclutas.reclutarUnHeroeAleatorio(carruaje);
        heroeReclutado.mostrarHeroe();

        assertThat(servicioReclutas.getCantidadDeHeroesEnUsuarioHeroeSegunCarruaje(carruaje), is(1));
        assertThat(servicioReclutas.getCantidadDeHeroesEnUsuarioHeroeSegunUsuario(carruaje.getUsuario()), is(1));
        assertThat(servicioReclutas.getCantidadDeHeroesDisponiblesEnCarruaje(carruaje), is(1));
    }

    @Test
    public void dadoCarruajeConDosHeroes_whenReclutoDosHeroes_entoncesObtengoDosHeroesAsignadosYningunoDisponible() {
        Carruaje carruaje = givenExisteCarruajeConUsuarioAsignadoYDosHeroes();

        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);
        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);

        assertThat(servicioReclutas.getCantidadDeHeroesEnUsuarioHeroeSegunCarruaje(carruaje), is(2));
        assertThat(servicioReclutas.getCantidadDeHeroesEnUsuarioHeroeSegunUsuario(carruaje.getUsuario()), is(2));
        assertThat(servicioReclutas.getCantidadDeHeroesDisponiblesEnCarruaje(carruaje), is(0));
    }


    @Test
    public void dadoSemana0YNivel0_yReclutoDosHeroes_cuandoConsultoHeroesDisponibles_entoncesDevuelve0() {
        Carruaje carruaje = givenExisteCarruajeConUsuarioAsignadoYDosHeroes();

        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);
        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);

        assertThat(servicioReclutas.getCantidadDeHeroesDisponiblesEnCarruaje(carruaje), is(0));
    }

    @Test
    public void siReclutoUnHeroeSegunID__CarruajeDevuelve1HeroeDisponible() {
        Carruaje carruaje = givenExisteCarruajeConUsuarioAsignadoYDosHeroes();

        servicioReclutas.reclutarSegunIdHeroe(1L);

        assertThat(servicioReclutas.getCantidadDeHeroesEnUsuarioHeroeSegunCarruaje(carruaje),is(1));
        assertThat(servicioReclutas.getCantidadDeHeroesDisponiblesEnCarruaje(carruaje), is(1));
    }

    @Test
    public void siRecluto2HeroesSegunID__CarruajeDevuelve0HeroeDisponible() {
        Carruaje carruaje = givenExisteCarruajeConUsuarioAsignadoYDosHeroes();

        servicioReclutas.reclutarSegunIdHeroe(1L);
        servicioReclutas.reclutarSegunIdHeroe(2L);

        assertThat(servicioReclutas.getCantidadDeHeroesEnUsuarioHeroeSegunCarruaje(carruaje),is(2));
        assertThat(servicioReclutas.getCantidadDeHeroesDisponiblesEnCarruaje(carruaje), is(0));
    }


// ─────────────────────────────────────────────────────────────────────────────
// Tests de paso de semana y recálculo de héroes disponibles
// ─────────────────────────────────────────────────────────────────────────────

    @Test
    public void dadoSemana0_conDosReclutamientos_cuandoAvanzaASemana1_entoncesRestablece2HeroesDisponibles() {

        Carruaje carruaje = givenExisteCarruajeConUsuarioAsignadoYDosHeroes();

        // Semana 0 - reclutar 2 héroes
        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);
        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);

        // Avanza a semana 1
        servicioReclutas.pasarSemana(carruaje);

        assertThat(servicioReclutas.getCantidadDeHeroesDisponiblesEnCarruaje(carruaje), is(2));
    }



    @Test
    public void dadoSemana0_conUnReclutamiento_cuandoAvanzaASemana1_entoncesDevuelve3HeroesDisponibles() {
        // dado Semana 0 con nivel 0 y 2 héroes iniciales
        Carruaje carruaje = givenExisteCarruajeConUsuarioAsignadoYDosHeroes();

        // recluta 1 héroe en semana 0
        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);

        // avanza a semana 1
        servicioReclutas.pasarSemana(carruaje);

        assertThat(servicioReclutas.getCantidadDeHeroesDisponiblesEnCarruaje(carruaje), is(3));
    }


    @Test
    public void dadoSemana0_conUnReclutamientoYLuegoOtroEnSemana1_cuandoConsultoHeroesDisponibles_entoncesDevuelve2() {
        // dado Semana 0 con nivel 0 y 2 héroes iniciales
        Carruaje carruaje = givenExisteCarruajeConUsuarioAsignadoYDosHeroes();

        // Semana 0: recluta 1 héroe
        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);

        // Avanza a semana 1
        servicioReclutas.pasarSemana(carruaje);

        // Semana 1: recluta otro héroe
        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);

        // Ahora debieran quedar 2 disponibles (1 remanente + 1 reclutado)
        assertThat(servicioReclutas.getCantidadDeHeroesDisponiblesEnCarruaje(carruaje), is(2));
    }

    @Test
    public void dadoCarruajeNivel1_enSemana1_llegan3HeroesYQuedaUnReclutado_entoncesQuedan4Disponibles() {
        // dado Semana 0 con nivel 0 y 2 héroes iniciales
        Carruaje carruaje = givenExisteCarruajeConUsuarioAsignadoYDosHeroes();

        // Semana 0: recluta 1 héroe
        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);

        // Aumenta el nivel a 1 (3 héroes semanales a partir de la siguiente semana)
        servicioReclutas.aumentarNivel(carruaje);

        // Avanza a semana 1: llegan 3 nuevos héroes
        servicioReclutas.pasarSemana(carruaje);

        // Total disponibles = 1 remanente + 3 nuevos = 4
        assertThat(servicioReclutas.getCantidadDeHeroesDisponiblesEnCarruaje(carruaje), is(4));
        assertThat(servicioReclutas.getCantidadDeHeroesSemanalesSegunCarruaje(carruaje), is(3));
    }

    @Test
    public void dadoCarruajeNivel2_enSemana1_llegan4HeroesYUnoFueReclutado_entoncesQuedan4Disponibles() {
        // dado Semana 0 con nivel 0 y 2 héroes iniciales
        Carruaje carruaje = givenExisteCarruajeConUsuarioAsignadoYDosHeroes();

        // Semana 0: recluta 1 héroe
        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);

        // Aumenta el nivel a 1 (3 héroes semanales) y avanza a semana 1
        servicioReclutas.aumentarNivel(carruaje);
        servicioReclutas.pasarSemana(carruaje);

        // Ahora aumenta el nivel a 2 (4 héroes semanales, que se aplicarán en la próxima semana)
        servicioReclutas.aumentarNivel(carruaje);

        // Sin avanzar otra semana, la disponibilidad sigue siendo la que quedó en S1
        assertThat(servicioReclutas.getCantidadDeHeroesDisponiblesEnCarruaje(carruaje), is(4));
        assertThat(servicioReclutas.getCantidadDeHeroesSemanalesSegunCarruaje(carruaje), is(4));
    }

    @Test
    public void dadoCarruajeNivel2_enSemana2_llegan8HeroesYUnoFueReclutado_entoncesQuedan8Disponibles() {
        // dado Semana 0 con nivel 0 y 2 héroes iniciales
        Carruaje carruaje = givenExisteCarruajeConUsuarioAsignadoYDosHeroes();

        // Semana 0: recluta 1 héroe
        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);

        // Nivel → 1 (herosSemanales = 3) y semana 1
        servicioReclutas.aumentarNivel(carruaje);
        servicioReclutas.pasarSemana(carruaje);

        // Nivel → 2 (herosSemanales = 4) y semana 2
        servicioReclutas.aumentarNivel(carruaje);
        servicioReclutas.pasarSemana(carruaje);

        //carruaje.mostrarTotalHeroes();

        // En S2: 1 remanente + 3 nuevos de S1 + 4 nuevos de S2 = 8
        assertThat(servicioReclutas.getCantidadDeHeroesDisponiblesEnCarruaje(carruaje), is(8));
        assertThat(servicioReclutas.getCantidadDeHeroesSemanalesSegunCarruaje(carruaje), is(4));
    }
    
    @Test
    public void dadoCarruajeNivel2_conReclutamientosEnSemana0Y2_cuandoConsultoHeroesDisponibles_entoncesDevuelve5() {
        Carruaje carruaje = givenExisteCarruajeConUsuarioAsignadoYDosHeroes();

        // Semana 0: recluta 1 héroe
        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);//1hd

        // Nivel → 1 y pasa a semana 1
        servicioReclutas.aumentarNivel(carruaje);
        servicioReclutas.pasarSemana(carruaje);//4hd

        // Nivel → 2 y pasa a semana 2
        servicioReclutas.aumentarNivel(carruaje);
        servicioReclutas.pasarSemana(carruaje);//8hd

        // En S2: recluta 3 héroes más
        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);
        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);
        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);

        // Disponibles = 1 remanente + 3 (S1) + 4 (S2) – 3 reclutados = 5
        assertThat(servicioReclutas.getCantidadDeHeroesDisponiblesEnCarruaje(carruaje), is(5));
        assertThat(servicioReclutas.getCantidadDeHeroesSemanalesSegunCarruaje(carruaje), is(4));
    }

    @Test
    public void dadoCarruajeNivel2_conReclutamientosEnSemana0Y1_cuandoConsultoHeroesDisponibles_entoncesDevuelve3() {
        // Inicializar carruaje con 2 héroes en S0
        Carruaje carruaje = givenExisteCarruajeConUsuarioAsignadoYDosHeroes();

        // Semana 0: reclutar 1 héroe
        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);
        // Subir directamente a nivel 2 (herosSemanales = 4)
        servicioReclutas.aumentarNivel(carruaje);
        servicioReclutas.aumentarNivel(carruaje);

        // Avanzar a semana 1: llegan 4 héroes
        servicioReclutas.pasarSemana(carruaje);
        // Semana 1: reclutar 2 héroes
        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);
        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);

        // Disponibles = 1 remanente + 4 nuevos - 2 reclutados = 3
        assertThat(servicioReclutas.getCantidadDeHeroesDisponiblesEnCarruaje(carruaje), is(3));
        assertThat(servicioReclutas.getCantidadDeHeroesSemanalesSegunCarruaje(carruaje), is(4));

    }

    @Test
    public void __dadoCarruajeNivel2_conReclutamientosEnSemana0Y1_cuandoConsultoHeroesDisponibles_entoncesDevuelve3() {
        // Inicializar carruaje con 2 héroes en S0
        Carruaje carruaje = givenExisteCarruajeConUsuarioAsignadoYDosHeroes();

        // Semana 0: reclutar 1 héroe
        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);
        // Subir directamente a nivel 2 (herosSemanales = 4)
        servicioReclutas.aumentarNivel(carruaje);
        servicioReclutas.aumentarNivel(carruaje);

        // Avanzar a semana 1: llegan 4 héroes
        servicioReclutas.pasarSemana(carruaje);
        // Semana 1: reclutar 2 héroes
        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);
        servicioReclutas.reclutarUnHeroeAleatorio(carruaje);

        // Disponibles = 1 remanente + 4 nuevos - 2 reclutados = 3
        assertThat(servicioReclutas.getCantidadDeHeroesDisponiblesEnCarruaje(carruaje), is(3));
        assertThat(servicioReclutas.getCantidadDeHeroesSemanalesSegunCarruaje(carruaje), is(4));

    }


    private Carruaje givenExisteCarruajeConUsuarioAsignadoYDosHeroes() {

        givenExiste19HeroesMasEnLaBaseDeDatos();

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("jugador1");

        Carruaje carruaje = servicioReclutas.getAsignarCarruajeAUsuario(usuario);//s0,n0
        servicioReclutas.agregarCarruaje(carruaje);

        servicioReclutas.setSemanaEnUsuarioSemanaSegunCarruaje(carruaje);


        return carruaje;
    }



    private void givenExiste19HeroesMasEnLaBaseDeDatos() {

        servicioReclutas.setHeroe(new Heroe(1L,"her1"));
        servicioReclutas.setHeroe(new Heroe(2L,"her2"));
        servicioReclutas.setHeroe(new Heroe(3L, "her3"));
        servicioReclutas.setHeroe(new Heroe(4L, "her4"));
        servicioReclutas.setHeroe(new Heroe(5L, "her5"));
        servicioReclutas.setHeroe(new Heroe(6L, "her6"));
        servicioReclutas.setHeroe(new Heroe(7L, "her7"));
        servicioReclutas.setHeroe(new Heroe(8L, "her8"));
        servicioReclutas.setHeroe(new Heroe(9L, "her9"));
        servicioReclutas.setHeroe(new Heroe(10L, "her10"));
        servicioReclutas.setHeroe(new Heroe(11L, "her11"));
        servicioReclutas.setHeroe(new Heroe(12L, "her12"));
        servicioReclutas.setHeroe(new Heroe(13L, "her13"));
        servicioReclutas.setHeroe(new Heroe(14L, "her14"));
        servicioReclutas.setHeroe(new Heroe(15L, "her15"));
        servicioReclutas.setHeroe(new Heroe(16L, "her16"));
        servicioReclutas.setHeroe(new Heroe(17L, "her17"));
        servicioReclutas.setHeroe(new Heroe(18L, "her18"));
        servicioReclutas.setHeroe(new Heroe(19L, "her19"));


    }



}
