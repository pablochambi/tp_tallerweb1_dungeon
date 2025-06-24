package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Item;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.servicios.ServicioTienda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ControladorTienda {


    @Autowired
    private ServicioTienda servicioTienda;

//    @PostMapping("/comprar")
//    public String comprar(@RequestParam Long itemId, HttpServletRequest request, RedirectAttributes redirectAttributes) {
//        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
//
//        try {
//            servicioTienda.comprar(usuario, itemId);
//            redirectAttributes.addFlashAttribute("mensaje", "Compra exitosa!");
//        } catch (RuntimeException e) {
//            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
//        }
//
//        return "redirect:/tienda/ver";
//    }

    @PostMapping("/comprar")
    public ModelAndView comprar(@RequestParam Long itemId, HttpServletRequest request) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        ModelAndView mav = new ModelAndView("tienda");
        List<Item> items = servicioTienda.obtenerItemsDeUsuario(usuario); // Traigo de nuevo los ítems del usuario
        mav.addObject("items", items);

        try {
            servicioTienda.comprar(usuario, itemId);
            mav.addObject("mensaje", "Compra exitosa!");
        } catch (RuntimeException e) {
            mav.addObject("mensaje", e.getMessage());
        }

        return mav;
    }

    @GetMapping("/ver")
    public ModelAndView verTienda(HttpServletRequest request) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        List<Item> items = servicioTienda.obtenerItemsDeUsuario(usuario);

        ModelAndView mav = new ModelAndView("tienda");
        mav.addObject("items", items);
        mav.addObject("mensaje", "Bienvenido a la tienda!"); // mensaje fijo o dinámico
        return mav;
    }
}
