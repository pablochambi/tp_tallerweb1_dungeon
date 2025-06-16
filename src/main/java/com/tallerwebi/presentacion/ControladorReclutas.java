package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ReclutaException;
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
    private static final String REDIRECT_VISTA_CARRUAJE = "redirect:/carruaje";

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
        if (!esUsuarioAutenticado(request.getSession())) {
            return new ModelAndView(REDIRECT_LOGIN);
        }
        ModelMap model = new ModelMap();
        Usuario usuario = obtenerUsuarioDeSesion(request.getSession());
        Carruaje carruaje = servicioRecluta.asignarOActualizarUnCarrujeAUnUsuario(usuario.getId());


        try {
            List<Heroe> heroesEnCarruaje = servicioRecluta.getHeroesDisponiblesEnCarruaje(carruaje);

            if (heroesEnCarruaje == null) {
                heroesEnCarruaje = new ArrayList<>();
            }

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

        if (!esUsuarioAutenticado(request.getSession())) {
            return new ModelAndView(REDIRECT_LOGIN);
        }
        ModelMap model = new ModelMap();
        Usuario usuario = obtenerUsuarioDeSesion(request.getSession());
        Carruaje carruaje = servicioRecluta.asignarOActualizarUnCarrujeAUnUsuario(usuario.getId());

        try {

            List<Heroe> heroesAntes = servicioRecluta.getHeroesDisponiblesEnCarruaje(carruaje);

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
        if (!esUsuarioAutenticado(request.getSession())) {
            return new ModelAndView(REDIRECT_LOGIN);
        }
        ModelMap model = new ModelMap();
        Usuario usuario = obtenerUsuarioDeSesion(request.getSession());

        try {
            List<Heroe> listaObtenida = servicioRecluta.getHeroesObtenidosPorElUsuario(usuario);

            if (listaObtenida == null) {
                listaObtenida = new ArrayList<>();
            }

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
    
    
    private boolean esUsuarioAutenticado(HttpSession session) {
        return session.getAttribute(USUARIO) != null;
    }

    private Usuario obtenerUsuarioDeSesion(HttpSession session) {
        return (Usuario) session.getAttribute(USUARIO);
    }

    
}

//@Controller
//public class ControladorReclutas {
//
//    private final ServicioRecluta servicioRecluta;
//
//    @Autowired
//    public ControladorReclutas(ServicioRecluta servicioRecluta) {
//        this.servicioRecluta = servicioRecluta;
//    }
//
//    //Comentario
//
//    @GetMapping("/carruaje")
//    public ModelAndView mostrarCarruaje(HttpServletRequest request) {
//
//        if (request.getSession().getAttribute("usuario") == null) {
//            return new ModelAndView("redirect:/login");
//        }
//
//        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
//        Carruaje carruaje = servicioRecluta.asignarOActualizarUnCarrujeAUnUsuario(usuario.getId());
//
//        List<Heroe> heroesEnCarruaje  = servicioRecluta.getHeroesDisponiblesEnCarruaje(carruaje);
//
//
//        ModelMap model = new ModelMap();
//        model.put("carruaje", carruaje);
//        model.put("usuario", usuario);
//        model.put("heroesEnCarruaje", heroesEnCarruaje);
//
//
//        return new ModelAndView("vista_carruaje",model);
//    }
//
//
//    @GetMapping("/reclutar/{id}")
//    public ModelAndView reclutarHeroe(@PathVariable Long id, HttpServletRequest request) {
//
//        if (request.getSession().getAttribute("usuario") == null) {
//            return new ModelAndView("redirect:/login");
//        }
//
//        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
//        Carruaje carruaje = servicioRecluta.asignarOActualizarUnCarrujeAUnUsuario(usuario.getId());
//
//        servicioRecluta.quitarUnHeroeDelCarruaje(id,carruaje);
//        servicioRecluta.agregarUnHeroeAlUsuario(id, usuario);
//        List<Heroe> heroesEnCarruaje = servicioRecluta.getListaDeHeroesEnCarruaje(carruaje);
//
//
//        ModelMap model = new ModelMap();
//        model.put("carruaje", carruaje);
//        model.put("heroesEnCarruaje",heroesEnCarruaje);
//
//        return new ModelAndView("vista_carruaje",model);
//    }
//}


//    private  void agregarUnCarruajeYUsuarioALaSession(HttpServletRequest request) {
//
//        HttpSession session = request.getSession(); // Crea sesión si no existe
//
//        Usuario usuario = (Usuario) session.getAttribute("usuario");
//        session.setAttribute("usuario", usuario);
//    }


//

