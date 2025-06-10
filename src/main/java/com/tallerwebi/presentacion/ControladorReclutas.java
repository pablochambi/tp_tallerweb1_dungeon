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

    try {
            agregarUnCarruajeYUsuarioALaSession(request);

    } catch (Exception e) {
        ModelMap model = new ModelMap();
        model.put("error", "Error al mostrar el carruaje: " + e.getMessage());
        return new ModelAndView("error", model); // Redirect to an error page
    }

            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
            Carruaje carruaje = servicioRecluta.getCarruajeDelUsuarioId(usuario.getId());

//        try {

            List<Heroe> heroesEnCarruaje = servicioRecluta.getHeroesDisponiblesEnCarruaje(carruaje);

            ModelMap model = new ModelMap();
            model.put("carruaje", carruaje);
            model.put("usuario", carruaje.getUsuario());
            model.put("heroesEnCarruaje", heroesEnCarruaje);
            model.put("semana", carruaje.getSemana());
            model.put("nivel", carruaje.getNivel());
            model.put("heroesSemanales", carruaje.getCantidadDeHeroesSemanales());
            return new ModelAndView("vista_carruaje", model);

//        } catch (Exception e) {
//            ModelMap model = new ModelMap();
//            model.put("error", "Error al mostrar el carruaje: " + e.getMessage());
//            return new ModelAndView("error", model); // Redirect to an error page
//        }


    }

    @GetMapping("/reclutar/{id}")
    public ModelAndView reclutarHeroe(@PathVariable Long id, HttpServletRequest request) {
        agregarUnCarruajeYUsuarioALaSession(request);

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Carruaje carruaje = servicioRecluta.getCarruajeDelUsuarioId(usuario.getId());

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
//        Usuario usuarioRegistrado = servicioRecluta.registrarUnUsuario(new Usuario(1L,"admin@admin.com"));

            Usuario usuarioRegistrado = servicioRecluta.getUsuarioPorId(1L);
            HttpSession session = request.getSession();
            session.setAttribute("usuario", usuarioRegistrado);

            Usuario usuarioDeSession = (Usuario) session.getAttribute("usuario");


            if (usuarioDeSession != null) {

                try {
                Carruaje carruaje = servicioRecluta.asignarOActualizarUnCarrujeAUnUsuario(usuarioDeSession.getId());
                    session.setAttribute("carruaje", carruaje);
                } catch (Exception e) {
                    throw new RuntimeException("Error al configurar la sesión del usuario y carruaje", e);
                }

            }




//        if (session == null || session.getAttribute("usuario") == null) {
//            return new ModelAndView("redirect:/login");
//        }

    }





}
