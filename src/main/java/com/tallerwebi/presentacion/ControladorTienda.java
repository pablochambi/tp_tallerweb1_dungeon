package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Item;
import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.RepositorioItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ControladorTienda {

    private Jugador jugador = new Jugador("Jugador1", 150); // Simulado

    // Lista de ítems simulada con IDs
    private List<Item> itemsDisponibles = new ArrayList<>();

    public ControladorTienda() {
        itemsDisponibles.add(new Item(1L, "Poción de vida", "pocion", 50));
        itemsDisponibles.add(new Item(2L, "Espada", "arma", 100));
        itemsDisponibles.add(new Item(3L, "Casco", "armadura", 75));
    }

    @Autowired
    private RepositorioItem repositorioItem;

    @GetMapping("/tienda")
    public String mostrarTienda(Model model, HttpSession session) {
        Jugador jugador = (Jugador) session.getAttribute("jugador");

        if (jugador == null) {
            jugador = new Jugador();
            jugador.setOro(1000);
            session.setAttribute("jugador", jugador);
        }

        model.addAttribute("items", repositorioItem.obtenerTodosLosItems());
        model.addAttribute("jugador", jugador);
        return "tienda";
    }


    @PostMapping("/comprar")
    public String comprarItem(@RequestParam Long itemId, Model model, HttpSession session) {
        Item item = repositorioItem.obtenerPorId(itemId);
        Jugador jugador = (Jugador) session.getAttribute("jugador");

        if (jugador == null) {
            jugador = new Jugador(); // o redirigir a login si es por usuario real
            jugador.setOro(1000);
        }

        if (jugador.getOro() >= item.getPrecio()) {
            jugador.setOro(jugador.getOro() - item.getPrecio());
            model.addAttribute("mensaje", "Has comprado " + item.getNombre() + " por " + item.getPrecio() + " oro.");
        } else {
            model.addAttribute("error", "No tenés suficiente oro.");
        }

        // actualizar la sesión
        session.setAttribute("jugador", jugador);

        // Volver a mostrar la tienda con el nuevo oro
        model.addAttribute("items", repositorioItem.obtenerTodosLosItems());
        model.addAttribute("jugador", jugador);
        return "tienda";
    }
}
