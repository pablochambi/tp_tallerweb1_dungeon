package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.GameSession;
import com.tallerwebi.dominio.ServicioJuego;
import com.tallerwebi.dominio.entidades.SessionHero;
import com.tallerwebi.dominio.entidades.SessionMonster;
import com.tallerwebi.dominio.entidades.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ControladorJuego {

    private final ServicioJuego servicioJuego;

    @Autowired
    public ControladorJuego(ServicioJuego servicioJuego) {
        this.servicioJuego = servicioJuego;
    }

    @GetMapping("/juego")
    public String mostrarJuego(Model model) {
        GameSession session = servicioJuego.getSession();
        List<SessionMonster> monstruos = servicioJuego.getMonstruos();
        List<SessionHero>    heroes    = servicioJuego.getHeroesDeSesion();


        model.addAttribute("session",   session);
        model.addAttribute("heroes",    heroes);
        model.addAttribute("monstruos", monstruos);
        return "juego";
    }

    @PostMapping("/juego/atacar")
    public String atacar(
            @RequestParam("heroOrden")    int heroOrden,
            @RequestParam("monsterOrden") int monsterOrden,
            RedirectAttributes ra
    ) {
        String mensaje = servicioJuego.atacar(heroOrden, monsterOrden);
        ra.addFlashAttribute("mensaje", mensaje);
        return "redirect:/juego";
    }

    @PostMapping("/juego/defender")
    public String defender(
            @RequestParam("heroOrden") int heroOrden,
            RedirectAttributes ra
    ) {
        String mensaje = servicioJuego.defender(heroOrden);
        ra.addFlashAttribute("mensaje", mensaje);
        return "redirect:/juego";
    }

    @PostMapping("/juego/usarPocion")
    public String usarPocion(
            @RequestParam("heroOrden") int heroOrden,
            RedirectAttributes ra
    ) {
        String mensaje = servicioJuego.usarPocion(heroOrden);
        ra.addFlashAttribute("mensaje", mensaje);
        return "redirect:/juego";
    }

    @PostMapping("/juego/reiniciar")
    public String reiniciarMazmorra(RedirectAttributes ra) {
        ra.addFlashAttribute("mensaje", "Mazmorra reiniciada!");
        return "redirect:/juego";
    }

    @PostMapping("/juego/siguiente")
    public String siguienteMazmorra(RedirectAttributes ra) {
        servicioJuego.reiniciarMazmorra();
        ra.addFlashAttribute("mensaje", "¡Nueva mazmorra generada!");
        return "redirect:/juego";
    }
}