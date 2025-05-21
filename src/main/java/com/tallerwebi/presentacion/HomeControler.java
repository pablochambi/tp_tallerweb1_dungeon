package com.tallerwebi.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeControler {

    @GetMapping("/home")
    public ModelAndView irAMiHome(HttpServletRequest request) {

        String rol = (String) request.getSession().getAttribute("ROL");
        if (rol.equals("USER")){
            return new ModelAndView("home");
        }else{
            return new ModelAndView("redirect:/login");
        }

    }

    @GetMapping("/seleccion-heroes")
    public ModelAndView irAHeroes(HttpServletRequest request) {
        String rol = (String) request.getSession().getAttribute("ROL");
        if (rol.equals("USER")){
            return new ModelAndView("seleccion-heroes");
        }else{
            return new ModelAndView("redirect:/login");
        }

    }

    @GetMapping("/mazmorra")
    public ModelAndView irAMazmorra(HttpServletRequest request) {
        String rol = (String) request.getSession().getAttribute("ROL");
        if (rol.equals("USER")){
            return new ModelAndView("mazmorra");
        }else{
            return new ModelAndView("redirect:/login");
        }

    }

    @GetMapping("/reglas")
    public ModelAndView irAreglas(HttpServletRequest request) {
        String rol = (String) request.getSession().getAttribute("ROL");
        if (rol.equals("USER")){
            return new ModelAndView("reglas");
        }else{
            return new ModelAndView("redirect:/login");
        }

    }

}
