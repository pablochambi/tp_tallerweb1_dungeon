package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Item;
import com.tallerwebi.dominio.entidades.Jugador;
import com.tallerwebi.dominio.ServicioTienda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/tienda")
public class ControladorTienda {

    @Autowired
    private ServicioTienda servicioTienda;

    @GetMapping
    public String mostrarTienda(Model model, HttpSession session) {
        // Recuperar o inicializar el jugador en sesión
        Jugador jugador = (Jugador) session.getAttribute("jugador");
        if (jugador == null) {
            jugador = new Jugador();
            jugador.setOro(1000);
            session.setAttribute("jugador", jugador);
        }

        // Obtener datos via servicio
        List<Item> items = servicioTienda.obtenerItems();

        model.addAttribute("jugador", jugador);
        model.addAttribute("items", items);
        return "tienda";
    }

    @PostMapping("/comprar")
    public String comprar(@RequestParam Long itemId,
                          HttpSession session,
                          RedirectAttributes ra) {
        Jugador jugador = (Jugador) session.getAttribute("jugador");
        String mensaje = servicioTienda.comprarItem(itemId, jugador);
        // Actualizar sesión con los cambios en Jugador
        session.setAttribute("jugador", jugador);
        ra.addFlashAttribute("mensaje", mensaje);
        return "redirect:/tienda";
    }
}