package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.GameSession;
import com.tallerwebi.dominio.ServicioJuego;
import com.tallerwebi.dominio.SessionMonster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/juego")
public class ControladorJuego {

    @Autowired
    private ServicioJuego servicio;

    private static final int MONSTRUOS_POR_RONDA   = 3;
    private static final String ATTR_MONST_IDS     = "selectedMonsterIds";

    /** GET /juego */
    @GetMapping
    public String verJuego(Model model, HttpSession session) {

        // model.addAttribute("heroes",  servicio.getHeroes()); // POR AHORA NO HAY ENTIDAD HEROE
        model.addAttribute("session", servicio.getSession());

        // 2) Si no tenemos IDs en sesión, barajamos 3 monstruos y guardamos sus IDs
        @SuppressWarnings("unchecked")
        List<Long> monsterIds = (List<Long>) session.getAttribute(ATTR_MONST_IDS);

        if (monsterIds == null) {
            List<SessionMonster> todos = servicio.getMonstruos();
            Collections.shuffle(todos);
            List<SessionMonster> seleccion = todos.stream()
                    .limit(MONSTRUOS_POR_RONDA)
                    .collect(Collectors.toList());
            monsterIds = seleccion.stream()
                    .map(SessionMonster::getMonsterId)
                    .collect(Collectors.toList());
            session.setAttribute(ATTR_MONST_IDS, monsterIds);
        }

        // 3) Cargar fresh de la BD y filtrar por esos IDs
        List<SessionMonster> actuales = servicio.getMonstruos();
        List<Long> finalMonsterIds = monsterIds;
        List<SessionMonster> seleccion = actuales.stream()
                .filter(sm -> finalMonsterIds.contains(sm.getMonsterId()))
                .collect(Collectors.toList());
        model.addAttribute("monstruos", seleccion);

        return "juego";
    }

    /** POST /juego/atacar */
    @PostMapping("/atacar")
    public String atacar(
            @RequestParam("orden") int orden,
            Model model,
            HttpSession session
    ) {
        // Aplicar ataque
        String mensaje = servicio.atacar(orden);

        // Recargar héroes y sesión
        //model.addAttribute("heroes",   servicio.getHeroes());
        model.addAttribute("session",  servicio.getSession());
        model.addAttribute("mensaje",  mensaje);

        // Recuperar IDs y volver a cargar los mismos 3 monstruos
        @SuppressWarnings("unchecked")
        List<Long> monsterIds = (List<Long>) session.getAttribute(ATTR_MONST_IDS);
        List<SessionMonster> actuales = servicio.getMonstruos();
        List<SessionMonster> seleccion = actuales.stream()
                .filter(sm -> monsterIds.contains(sm.getMonsterId()))
                .collect(Collectors.toList());
        model.addAttribute("monstruos", seleccion);

        return "juego";
    }

    /** POST /juego/defender */
    @PostMapping("/defender")
    public String defender(Model model, HttpSession session) {
        String mensaje = servicio.defender();
       // model.addAttribute("heroes",   servicio.getHeroes());
        model.addAttribute("session",  servicio.getSession());
        model.addAttribute("mensaje",  mensaje);

        @SuppressWarnings("unchecked")
        List<Long> monsterIds = (List<Long>) session.getAttribute(ATTR_MONST_IDS);
        List<SessionMonster> actuales = servicio.getMonstruos();
        List<SessionMonster> seleccion = actuales.stream()
                .filter(sm -> monsterIds.contains(sm.getMonsterId()))
                .collect(Collectors.toList());
        model.addAttribute("monstruos", seleccion);

        return "juego";
    }

    /** POST /juego/usarPocion */
    @PostMapping("/usarPocion")
    public String usarPocion(Model model, HttpSession session) {
        String mensaje = servicio.usarPocion();
        //model.addAttribute("heroes",   servicio.getHeroes());
        model.addAttribute("session",  servicio.getSession());
        model.addAttribute("mensaje",  mensaje);

        @SuppressWarnings("unchecked")
        List<Long> monsterIds = (List<Long>) session.getAttribute(ATTR_MONST_IDS);
        List<SessionMonster> actuales = servicio.getMonstruos();
        List<SessionMonster> seleccion = actuales.stream()
                .filter(sm -> monsterIds.contains(sm.getMonsterId()))
                .collect(Collectors.toList());
        model.addAttribute("monstruos", seleccion);

        return "juego";
    }

    @PostMapping("/newGame")
    public String newGame(HttpSession httpSession) {
        // 1) Borrar la lista de monstruos elegidos
        httpSession.removeAttribute(ATTR_MONST_IDS);

        // 2) Finalizar (eliminar) la GameSession activa
        GameSession current = (GameSession) servicio.getSession();
        if (current != null) {
            servicio.endSession(current);
        }

        // 3) Redirigir a /juego → GET verJuego() creará nueva partida
        return "redirect:/juego";
    }

}
