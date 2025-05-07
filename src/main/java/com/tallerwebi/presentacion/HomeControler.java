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

}
