package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Carruaje;
import com.tallerwebi.dominio.entidades.Heroe;
import com.tallerwebi.dominio.ServicioRecluta;
import com.tallerwebi.dominio.entidades.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ControladorReclutas {

    private ServicioRecluta servicioRecluta;

    @Autowired
    public ControladorReclutas(ServicioRecluta servicioRecluta) {
        this.servicioRecluta = servicioRecluta;
    }

    @GetMapping("/carruaje")
    public ModelAndView mostrarCarruaje(HttpServletRequest request) {
        agregarUnCarruajeYUsuarioALaSession(request);
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Carruaje carruaje = servicioRecluta.getCarruajeDelUsuarioPorId(usuario.getId());

        List<Heroe> heroesEnCarruaje  = servicioRecluta.getHeroesDisponiblesEnCarruaje(carruaje);


        ModelMap model = new ModelMap();
        model.put("carruaje", carruaje);
        model.put("usuario", carruaje.getUsuario());
//        model.put("usuario", usuario);
        model.put("heroesEnCarruaje", heroesEnCarruaje);

//        model.put("semana", carruaje.getSemana());
//        model.put("nivel", carruaje.getNivel());
//        model.put("heroesSemanales", carruaje.getCantidadDeHeroesSemanales());


        return new ModelAndView("vista_carruaje",model);
    }

    @GetMapping("/reclutar/{id}")
    public ModelAndView reclutarHeroe(@PathVariable Long id, HttpServletRequest request) {

        agregarUnCarruajeYUsuarioALaSession(request);

        Carruaje carruaje = (Carruaje) request.getSession().getAttribute("carruaje");
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        servicioRecluta.quitarUnHeroeDelCarruaje(id,carruaje);
        servicioRecluta.agregarUnHeroeAlUsuario(id, usuario);
        List<Heroe> heroesEnCarruaje = servicioRecluta.getListaDeHeroesEnCarruaje(carruaje);

        ModelMap model = new ModelMap();
        model.put("carruaje", carruaje);
        model.put("heroesEnCarruaje",heroesEnCarruaje);

        return new ModelAndView("vista_carruaje",model);
    }

    private  void agregarUnCarruajeYUsuarioALaSession(HttpServletRequest request) {
        // 1. Crear/obtener sesión // 2. Guardar atributos en sesión
        Usuario usuarioRegistrado = servicioRecluta.getUsuarioRegistradoPorId(2L);//
        HttpSession session = request.getSession(); // Crea sesión si no existe
        session.setAttribute("usuario", usuarioRegistrado);

        Usuario usuarioDeSession = (Usuario) session.getAttribute("usuario");

        if(usuarioDeSession!=null) {
            Carruaje carruaje = servicioRecluta.asignarOActualizarUnCarrujeAUnUsuario(usuarioDeSession.getId());
            session.setAttribute("carruaje", carruaje);
        }



//        if (session == null || session.getAttribute("usuario") == null) {
//            return new ModelAndView("redirect:/login");
//        }

    }


//    public ModelAndView aumentarNivel(Carruaje carr) {
//
//        ModelMap modelo = new ModelMap();
//
//        servicioRecluta.aumentarNivel(carr);
//
//        modelo.put("carruaje",carr);
//        modelo.put("usuario",carr.getUsuario());
//
//        return new ModelAndView("carruaje", modelo);
//
//    }



}
