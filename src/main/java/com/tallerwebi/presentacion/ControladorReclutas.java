package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Carruaje;
import com.tallerwebi.dominio.entidades.Heroe;
import com.tallerwebi.dominio.ServicioRecluta;
import com.tallerwebi.dominio.entidades.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class ControladorReclutas {

    private final ServicioRecluta servicioRecluta;

    @Autowired
    public ControladorReclutas(ServicioRecluta servicioRecluta) {
        this.servicioRecluta = servicioRecluta;
    }

    @GetMapping("/carruaje")
    public ModelAndView mostrarCarruaje(HttpServletRequest request) {

        if (request.getSession().getAttribute("usuario") == null) {
            return new ModelAndView("redirect:/login");
        }

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Carruaje carruaje = servicioRecluta.asignarOActualizarUnCarrujeAUnUsuario(usuario.getId());

        List<Heroe> heroesEnCarruaje  = servicioRecluta.getHeroesDisponiblesEnCarruaje(carruaje);


        ModelMap model = new ModelMap();
        model.put("carruaje", carruaje);
        model.put("usuario", usuario);
        model.put("heroesEnCarruaje", heroesEnCarruaje);


        return new ModelAndView("vista_carruaje",model);
    }


    @GetMapping("/reclutar/{id}")
    public ModelAndView reclutarHeroe(@PathVariable Long id, HttpServletRequest request) {

        if (request.getSession().getAttribute("usuario") == null) {
            return new ModelAndView("redirect:/login");
        }

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Carruaje carruaje = servicioRecluta.asignarOActualizarUnCarrujeAUnUsuario(usuario.getId());

        servicioRecluta.quitarUnHeroeDelCarruaje(id,carruaje);
        servicioRecluta.agregarUnHeroeAlUsuario(id, usuario);
        List<Heroe> heroesEnCarruaje = servicioRecluta.getListaDeHeroesEnCarruaje(carruaje);


        ModelMap model = new ModelMap();
        model.put("carruaje", carruaje);
        model.put("heroesEnCarruaje",heroesEnCarruaje);

        return new ModelAndView("vista_carruaje",model);
    }




    //    private  void agregarUnCarruajeYUsuarioALaSession(HttpServletRequest request) {
//
//        HttpSession session = request.getSession(); // Crea sesión si no existe
//
//        Usuario usuario = (Usuario) session.getAttribute("usuario");
//        session.setAttribute("usuario", usuario);
//    }


//
//@Controller
//public class ControladorReclutas {
//
//    private static final String VISTA_CARRUAJE = "vista_carruaje";
//    private static final String REDIRECT_LOGIN = "redirect:/login";
//    private static final String ATTR_USUARIO = "usuario";
//    private static final String ATTR_CARRUAJE = "carruaje";
//    private static final String ATTR_HEROES = "heroesEnCarruaje";
//
//    private final ServicioRecluta servicioRecluta;
//
//    @Autowired
//    public ControladorReclutas(ServicioRecluta servicioRecluta) {
//        this.servicioRecluta = servicioRecluta;
//    }
//
//    @GetMapping("/carruaje")
//    public ModelAndView mostrarCarruaje(HttpServletRequest request) {
//        if (!esUsuarioAutenticado(request.getSession())) {
//            return new ModelAndView(REDIRECT_LOGIN);
//        }
//
//        Usuario usuario = obtenerUsuarioDeSesion(request.getSession());
//        Carruaje carruaje = servicioRecluta.asignarOActualizarUnCarrujeAUnUsuario(usuario.getId());
//
//        List<Heroe> heroesEnCarruaje = servicioRecluta.getHeroesDisponiblesEnCarruaje(carruaje);
//
//        ModelMap model = new ModelMap();
//        model.put(ATTR_CARRUAJE, carruaje);
//        model.put(ATTR_USUARIO, usuario);
//        model.put(ATTR_HEROES, heroesEnCarruaje);
//
//        return new ModelAndView(VISTA_CARRUAJE, model);
//    }
//
//    @GetMapping("/reclutar/{id}")
//    public ModelAndView reclutarHeroe(@PathVariable("id") Long idHeroe, HttpServletRequest request) {
//        if (!esUsuarioAutenticado(request.getSession())) {
//            return new ModelAndView(REDIRECT_LOGIN);
//        }
//
//        Usuario usuario = obtenerUsuarioDeSesion(request.getSession());
//        Carruaje carruaje = servicioRecluta.asignarOActualizarUnCarrujeAUnUsuario(usuario.getId());
//
//        servicioRecluta.quitarUnHeroeDelCarruaje(idHeroe, carruaje);
//        servicioRecluta.agregarUnHeroeAlUsuario(idHeroe, usuario);
//
//        List<Heroe> heroesEnCarruaje = servicioRecluta.getListaDeHeroesEnCarruaje(carruaje);
//
//        ModelMap model = new ModelMap();
//
//        model.put(ATTR_CARRUAJE, carruaje);
//        model.put(ATTR_HEROES, heroesEnCarruaje);
//
//        return new ModelAndView(VISTA_CARRUAJE, model);
//    }
//
//    private boolean esUsuarioAutenticado(HttpSession session) {
//        return session.getAttribute(ATTR_USUARIO) != null;
//    }
//
//    private Usuario obtenerUsuarioDeSesion(HttpSession session) {
//        return (Usuario) session.getAttribute(ATTR_USUARIO);
//    }
//}



}
