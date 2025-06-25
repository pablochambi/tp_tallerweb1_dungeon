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

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ControladorJuego {

    private final ServicioJuego servicioJuego;

    @Autowired
    public ControladorJuego(ServicioJuego servicioJuego) {
        this.servicioJuego = servicioJuego;
    }

    @GetMapping("/juego")
    public String mostrarJuego(HttpSession httpSession, Model model) {
        // 0) Obtengo el usuario logueado de la sesión HTTP
        Usuario usuario = (Usuario) httpSession.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        // 1) Inicio o recupero la partida/expedición de ese usuario
        GameSession session = servicioJuego.iniciarPartida(usuario);

        // 2) Cargo los monstruos y los héroes de esa sesión
        List<SessionMonster> monstruos    = servicioJuego.getMonstruos(usuario);
        List<SessionHero>    heroesDeSesion = servicioJuego.getHeroesDeSesion(usuario);

        // 3) Los pongo en el modelo para Thymeleaf
        model.addAttribute("session",        session);
        model.addAttribute("usuario",        usuario);
        model.addAttribute("monstruos",      monstruos);
        model.addAttribute("heroesDeSesion", heroesDeSesion);

        return "juego";
    }

    @PostMapping("/juego/atacar")
    public String atacar(
            @RequestParam int heroOrden,
            @RequestParam int monsterOrden,
            HttpSession httpSession,
            RedirectAttributes ra
    ) {
        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        String mensaje = servicioJuego.atacar(u, heroOrden, monsterOrden);
        ra.addFlashAttribute("mensaje", mensaje);
        return "redirect:/juego";
    }

    @PostMapping("/juego/defender")
    public String defender(
            @RequestParam int heroOrden,
            HttpSession httpSession,
            RedirectAttributes ra
    ) {
        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        String mensaje = servicioJuego.defender(u, heroOrden);
        ra.addFlashAttribute("mensaje", mensaje);
        return "redirect:/juego";
    }

    @PostMapping("/juego/usarPocion")
    public String usarPocion(
            @RequestParam int heroOrden,
            HttpSession httpSession,
            RedirectAttributes ra
    ) {
        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        String mensaje = servicioJuego.usarPocion(u, heroOrden);
        ra.addFlashAttribute("mensaje", mensaje);
        return "redirect:/juego";
    }

    @PostMapping("/juego/siguiente")
    public String siguienteMazmorra(
            HttpSession httpSession,
            RedirectAttributes ra
    ) {
        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        servicioJuego.siguienteMazmorra(u);
        ra.addFlashAttribute("mensaje", "¡Nueva mazmorra generada!");
        return "redirect:/juego";
    }

}
