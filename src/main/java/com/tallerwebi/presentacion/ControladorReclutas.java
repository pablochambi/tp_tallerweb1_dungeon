package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Carruaje;
import com.tallerwebi.dominio.ServicioRecluta;
import com.tallerwebi.dominio.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorReclutas {

    private ServicioRecluta servicioRecluta;

    @Autowired
    public ControladorReclutas(ServicioRecluta servicioRecluta) {
        this.servicioRecluta = servicioRecluta;
    }

    public ModelAndView irACarruaje(Usuario usuario) {

        ModelMap modelo = new ModelMap();

        modelo.put("usuario", usuario);
        modelo.put("carruaje",servicioRecluta.getAsignarCarruajeAUsuario(usuario));


        return new ModelAndView("carruaje", modelo);

    }

    public ModelAndView aumentarNivel(Carruaje carr) {

        ModelMap modelo = new ModelMap();

        servicioRecluta.aumentarNivel(carr);

        modelo.put("carruaje",carr);
        modelo.put("usuario",carr.getUsuario());

        return new ModelAndView("carruaje", modelo);

    }

    public ModelAndView reclutarHeroe(Carruaje carr, Long idHeroe) {

        ModelMap modelo = new ModelMap();

        servicioRecluta.reclutarSegunIdHeroe(idHeroe);

        modelo.put("carruaje",carr);
        modelo.put("usuario",carr.getUsuario());

        return new ModelAndView("carruaje", modelo);

    }
}
