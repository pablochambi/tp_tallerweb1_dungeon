package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

import java.util.Collections;
import java.util.List;

import com.tallerwebi.dominio.entidades.*;
import com.tallerwebi.dominio.interfaces.*;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;


class ServicioJuegoImplTest {

    @Mock
    RepositorioUsuario usuarioRepo;
    @Mock
    RepositorioSession sessionRepo;
    @Mock
    RepositorioMonster monsterRepo;
    @Mock
    RepositorioSessionMonster smRepo;
    @Mock
    private RepositorioHeroSession shRepo;

    @InjectMocks ServicioJuegoImpl servicio;

    private Usuario usuario;
    private GameSession session;
    private Monster m1, m2, m3;
    private SessionMonster sm1, sm2, sm3;
    private SessionHero sh1;


    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        Heroe plantilla = new Heroe();
        plantilla.setId(1L);
        plantilla.setNombre("HéroeTest");
        plantilla.setMaxVida(100);
        plantilla.setAtk(10);

        // Sesión vacía
        session = new GameSession();
        session.setUsuario(usuario);

        // Monstruos base
        m1 = new Monster(); m1.setId(1L); m1.setNombre("Esqueleto"); m1.setVida(30); m1.setAtk(5);
        m2 = new Monster(); m2.setId(2L); m2.setNombre("Goblin");    m2.setVida(40); m2.setAtk(8);
        m3 = new Monster(); m3.setId(3L); m3.setNombre("Araña");     m3.setVida(20); m3.setAtk(3);

        // SessionMonster ejemplo
        sm1 = new SessionMonster(); sm1.setOrden(1); sm1.setVidaActual(30); sm1.setMonster(m1);
        sm2 = new SessionMonster(); sm2.setOrden(2); sm2.setVidaActual(40); sm2.setMonster(m2);
        sm3 = new SessionMonster(); sm3.setOrden(3); sm3.setVidaActual(20); sm3.setMonster(m3);
    }

    @Test
    void getJugador_creaYDevuelveElJugador() {
        // given
        when(sessionRepo.findActive()).thenReturn(null);
        when(usuarioRepo.buscarUsuarioPorId(1L)).thenReturn(usuario);

        // when
        Usuario resultado = servicio.getUsuario();

        // then
        assertThat(resultado, sameInstance(usuario));
        verify(usuarioRepo).buscarUsuarioPorId(1L);
        verify(sessionRepo).save(any(GameSession.class));
    }

    @Test
    void getMonstruos_primeraVez_sembrados3MonstruosAleatorios() {

        when(sessionRepo.findActive()).thenReturn(null);

        when(usuarioRepo.buscarUsuarioPorId(1L)).thenReturn(usuario);

        when(monsterRepo.obtenerTodosLosMonstruos())
                .thenReturn(List.of(m1, m2, m3, new Monster(), new Monster()));

        servicio.getMonstruos();

        verify(smRepo, times(3))
                .add(any(GameSession.class), any(Monster.class));
    }

    @Test
    void atacar_ordenNoExiste_devuelveMonstruoNoEncontrado() {
        // given
        when(sessionRepo.findActive()).thenReturn(session);
        when(smRepo.findBySession(session)).thenReturn(List.of(sm1, sm2));
        // when
        String mensaje = servicio.atacar(1, 99);
        // then
        assertThat(mensaje, is("Monstruo no encontrado."));
    }

    @Test
    void atacar_monstruoVivo_actualizaVidaYMensajeContieneNombre() {
        when(sessionRepo.findActive(usuario)).thenReturn(session);
        when(smRepo.findBySession(session)).thenReturn(List.of(sm1));
        when(shRepo.findBySession(session)).thenReturn(List.of(sh1));

        // when:
        String msg = servicio.atacar(1, 1);

        // then:
        verify(smRepo).update(argThat(s -> s.getVidaActual() == 20));

        verify(shRepo).update(argThat(h -> h.getVidaActual() == 95));

        // el mensaje incluye el nombre del monstruo
        assertThat(msg, containsString("Esqueleto"));
    }

    @Test
    void reiniciarMazmorra_borraYSemilla3Nuevos() {
        // given
        when(sessionRepo.findActive()).thenReturn(session);
        when(monsterRepo.obtenerTodosLosMonstruos()).thenReturn(List.of(m1,m2,m3));
        // when
        servicio.reiniciarMazmorra();
        // then: elimina antiguos y añade 3
        verify(smRepo).deleteBySession(session);
        verify(smRepo, times(3)).add(eq(session), any(Monster.class));
    }
}
