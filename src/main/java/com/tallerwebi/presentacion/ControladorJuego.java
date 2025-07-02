package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.GameSession;
import com.tallerwebi.dominio.servicios.ServicioJuego;
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
        GameSession session    = (GameSession) servicioJuego.getSession();
        Usuario usuario        = session.getUsuario();
        List<SessionMonster> monstruos = servicioJuego.getMonstruos();

        model.addAttribute("session",   session);
        model.addAttribute("usuario",   usuario);
        model.addAttribute("monstruos", monstruos);
        return "juego";
    }

    @PostMapping("/juego/atacar")
    public String atacar(@RequestParam int orden, RedirectAttributes ra) {
        String mensaje = servicioJuego.atacar(orden);
        ra.addFlashAttribute("mensaje", mensaje);
        return "redirect:/juego";
    }

    @PostMapping("/juego/defender")
    public String defender(RedirectAttributes ra) {
        String mensaje = servicioJuego.defender();
        ra.addFlashAttribute("mensaje", mensaje);
        return "redirect:/juego";
    }

    @PostMapping("/juego/usarPocion")
    public String usarPocion(RedirectAttributes ra) {
        String mensaje = servicioJuego.usarPocion();
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