package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.*;
import com.tallerwebi.dominio.ServicioJuego;
import com.tallerwebi.dominio.interfaces.RepositorioExpedition;
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
    private final RepositorioExpedition expeditionRepo;

    @Autowired
    public ControladorJuego(ServicioJuego servicioJuego, RepositorioExpedition expeditionRepo) {
        this.servicioJuego = servicioJuego;
        this.expeditionRepo = expeditionRepo;
    }

    @GetMapping("/juego")
    public String mostrarJuego(HttpSession httpSession, Model model) {
        Usuario usuario = (Usuario) httpSession.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        GameSession session = servicioJuego.iniciarPartida(usuario);

        Expedition exp = expeditionRepo
                .findBySessionAndCompletedFalse(session)
                .orElseThrow(() -> new IllegalStateException("No hay expedición activa"));

        List<SessionMonster> monstruos    = servicioJuego.getMonstruos(usuario);
        List<SessionHero>    heroesDeSes  = servicioJuego.getHeroesDeSesion(usuario);

        model.addAttribute("usuario",        usuario);
        model.addAttribute("session",        session);
        model.addAttribute("expNumber",      exp.getNumber());
        model.addAttribute("dungeonLevel", exp.getNumber());
        model.addAttribute("monstruos",      monstruos);
        model.addAttribute("heroesDeSesion", heroesDeSes);

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

    @PostMapping("/juego/terminarExpedicion")
    public String terminarExpedicion(HttpSession httpSession) {
        Usuario u = (Usuario) httpSession.getAttribute("usuario");
        if (u == null) return "redirect:/login";

        servicioJuego.terminarExpedicion(u);
        return "redirect:/home";  // o donde tengas tu “lobby”
    }


}
