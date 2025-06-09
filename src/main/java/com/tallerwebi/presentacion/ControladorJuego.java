package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioJuego;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/juego")
public class ControladorJuego {
    @Autowired
    ServicioJuego servicio;

    @GetMapping
    public String juego(Model m) {
        m.addAttribute("jugador", servicio.getJugador());
        m.addAttribute("monstruos", servicio.getMonstruos());
        return "juego";
    }

    @PostMapping("/atacar")
    public String atacar(@RequestParam int orden, Model m) {
        m.addAttribute("mensaje", servicio.atacar(orden));
        return "redirect:/juego";
    }
    // similares para defender y usarPocion
}