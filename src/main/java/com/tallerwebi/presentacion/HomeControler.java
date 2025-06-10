package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeControler {

    @GetMapping("/home")
    public ModelAndView irAMiHome(HttpServletRequest request) {

        String rol = (String) request.getSession().getAttribute("ROL");

        if (rol == null) {
            return new ModelAndView("redirect:/login");
        }

        ModelMap model = new ModelMap();
        model.put("rol", rol);

        return new ModelAndView("home", model);
    }

//    @GetMapping("/carruaje")
//    public ModelAndView irAReclutar(HttpServletRequest request) {
//        String rol = (String) request.getSession().getAttribute("ROL");
//        return new ModelAndView("vista_carruaje");
//    }


    @GetMapping("/seleccion-heroes")
    public ModelAndView irAHeroes() {
        return new ModelAndView("seleccion-heroes");
    }

    @GetMapping("/mazmorra")
    public ModelAndView irAMazmorra() {
        return new ModelAndView("mazmorra");
    }

    @GetMapping("/reglas")
    public ModelAndView irAreglas() {
        return new ModelAndView("reglas");
    }

}
