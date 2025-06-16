package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ReclutaException;
import com.tallerwebi.dominio.UsuarioNoAutenticadoException;
import com.tallerwebi.dominio.entidades.Carruaje;
import com.tallerwebi.dominio.entidades.Heroe;
import com.tallerwebi.dominio.ServicioRecluta;
import com.tallerwebi.dominio.entidades.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ControladorReclutas {

    private static final String VISTA_CARRUAJE = "vista_carruaje";
    private static final String REDIRECT_LOGIN = "redirect:/login";
    private static final String USUARIO = "usuario";
    private static final String CARRUAJE = "carruaje";
    private static final String HEROES = "heroesEnCarruaje";
    private static final String VISTA_HEROES_OBTENIDOS = "vista_heroes_obtenidos";
    private static final String HEROES_OBTENIDOS = "heroes_obtenidos";

    private final ServicioRecluta servicioRecluta;

    @Autowired
    public ControladorReclutas(ServicioRecluta servicioRecluta) {
        this.servicioRecluta = servicioRecluta;
    }

    @GetMapping("/carruaje")
    public ModelAndView mostrarCarruaje(HttpServletRequest request) {

        Usuario usuario = obtenerUsuarioDesdeSesion(request.getSession());

        if (usuario == null) return new ModelAndView(REDIRECT_LOGIN);

        Carruaje carruaje = servicioRecluta.asignarOActualizarUnCarrujeAUnUsuario(usuario.getId());

        ModelMap model = new ModelMap();

        try {
            List<Heroe> heroesEnCarruaje = servicioRecluta.getHeroesDisponiblesEnCarruaje(carruaje);

            model.put(CARRUAJE, carruaje);
            model.put(USUARIO, usuario);
            model.put(HEROES, heroesEnCarruaje);

            return new ModelAndView(VISTA_CARRUAJE, model);

        } catch (ReclutaException e) {
            // Manejo específico para excepciones de reclutamiento
            model.put(CARRUAJE, carruaje);
            model.put(USUARIO, usuario);
            model.put("mensaje1", e.getMessage());
            model.put("mensaje2", "Vuelve la próxima semana para más héroes.");
            return new ModelAndView(VISTA_CARRUAJE, model);

        }catch (Exception e) {
            // Manejo genérico para otros errores
            model.put("error", "Ocurrió un error inesperado");
            return new ModelAndView("error", model);
        }
    }

    @GetMapping("/reclutar/{id}")
    public ModelAndView reclutarHeroe(@PathVariable("id") Long idHeroe, HttpServletRequest request) {

        Usuario usuario = obtenerUsuarioDesdeSesion(request.getSession());

        if (usuario == null) return new ModelAndView(REDIRECT_LOGIN);

        Carruaje carruaje = servicioRecluta.asignarOActualizarUnCarrujeAUnUsuario(usuario.getId());
        ModelMap model = new ModelMap();

        try {
            servicioRecluta.quitarUnHeroeDelCarruaje(idHeroe, carruaje);
            servicioRecluta.agregarUnHeroeAlUsuario(idHeroe, usuario);

            List<Heroe> heroesDespues = servicioRecluta.getHeroesDisponiblesEnCarruaje(carruaje);

            model.put(CARRUAJE, carruaje);
            model.put(USUARIO, usuario);
            model.put(HEROES, heroesDespues);

            return new ModelAndView(VISTA_CARRUAJE, model);

        } catch (ReclutaException e) {
            // Manejo específico para excepciones de reclutamiento
            model.put(CARRUAJE, carruaje);
            model.put(USUARIO, usuario);
            model.put("mensaje1", e.getMessage());
            return new ModelAndView(VISTA_CARRUAJE, model);

        }catch (Exception e) {
            // Manejo genérico para otros errores
            model.put("error", "Ocurrió un error inesperado");
            return new ModelAndView("error", model);
        }


    }
    
    @GetMapping("/seleccion-heroes")
    public ModelAndView mostrarHeroesObtenidos(HttpServletRequest request) {

        Usuario usuario = obtenerUsuarioDesdeSesion(request.getSession());

        if (usuario == null) return new ModelAndView(REDIRECT_LOGIN);

        ModelMap model = new ModelMap();

        try {
            List<Heroe> listaObtenida = servicioRecluta.getHeroesObtenidosPorElUsuario(usuario);

            model.put(HEROES_OBTENIDOS, listaObtenida);
            model.put(USUARIO, usuario);

            return new ModelAndView(VISTA_HEROES_OBTENIDOS, model);

        }catch (ReclutaException e) {
            // Manejo específico para excepciones
            model.put(USUARIO, usuario);

            model.put("mensaje1", e.getMessage());

            return new ModelAndView(VISTA_HEROES_OBTENIDOS, model);

        }catch (Exception e) {
            // Manejo genérico para otros errores
            model.put("error", "Ocurrió un error inesperado");
            return new ModelAndView("error", model);
        }

        
    }

    private Usuario obtenerUsuarioDesdeSesion(HttpSession session) {
        Object usuario = session.getAttribute(USUARIO);
        if (usuario instanceof Usuario) {
            return (Usuario) usuario;
        }
        return null;
    }

    
}


