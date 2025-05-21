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
    public String mostrarTienda(Model model) {
        List<Item> items = repositorioItem.obtenerTodosLosItems();
        Jugador jugador = new Jugador();
        model.addAttribute("items", items);
        model.addAttribute("jugador", jugador);
        return "tienda";
    }

    @PostMapping("/comprar")
    public String comprarItem(@RequestParam Long itemId,
                              RedirectAttributes redirectAttributes) {

        Item itemSeleccionado = itemsDisponibles.stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElse(null);

        if (itemSeleccionado != null) {
            if (jugador.getOro() >= itemSeleccionado.getPrecio()) {
                jugador.setOro(jugador.getOro() - itemSeleccionado.getPrecio());
                jugador.getInventario().add(itemSeleccionado);
                redirectAttributes.addFlashAttribute("mensaje", "¡Compraste " + itemSeleccionado.getNombre() + "!");
            } else {
                redirectAttributes.addFlashAttribute("error", "No tenés suficiente oro.");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Ítem no encontrado.");
        }

        return "redirect:/tienda";
    }
}
