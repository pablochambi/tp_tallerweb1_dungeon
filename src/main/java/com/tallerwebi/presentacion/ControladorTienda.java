package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Item;
import com.tallerwebi.dominio.ServicioTienda;
import com.tallerwebi.dominio.entidades.Usuario;
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
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            usuario = new Usuario();
            usuario.setOro(1000);
            session.setAttribute("usuario", usuario);
        }

        // Obtener datos via servicio
        List<Item> items = servicioTienda.obtenerItems();

        model.addAttribute("usuario", usuario);
        model.addAttribute("items", items);
        return "tienda";
    }

    @PostMapping("/comprar")
    public String comprar(@RequestParam Long itemId,
                          HttpSession session,
                          RedirectAttributes ra) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String mensaje = servicioTienda.comprarItem(itemId, usuario);
        // Actualizar sesión con los cambios en Jugador
        session.setAttribute("usuario", usuario);
        ra.addFlashAttribute("mensaje", mensaje);
        return "redirect:/tienda";
    }
}