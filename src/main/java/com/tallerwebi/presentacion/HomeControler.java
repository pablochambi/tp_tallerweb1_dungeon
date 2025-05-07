package com.tallerwebi.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeControler {

    @GetMapping("/home")
    public ModelAndView irAMiHome() {
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
