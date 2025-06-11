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

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        if (usuario == null) {
            return new ModelAndView("redirect:/login");
        }

        return new ModelAndView("home");
    }

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
