package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Item;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.servicios.ServicioLogin;
import com.tallerwebi.dominio.servicios.ServicioTienda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ControladorTienda {



    private ServicioTienda servicioTienda;
    private ServicioLogin servicioLogin;

    @Autowired
    public ControladorTienda(ServicioTienda servicioTienda, ServicioLogin servicioLogin) {
        this.servicioTienda = servicioTienda;
        this.servicioLogin = servicioLogin;
    }



    @PostMapping("/tienda/comprar")
    public String comprar(@RequestParam Long itemId, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        //ModelAndView mav = new ModelAndView("tienda");
        ModelMap model = new ModelMap();
        model.addAttribute("usuario",usuario);

        List<Item> itemsDeTienda = servicioTienda.obtenerItemsDeTienda();
        model.addAttribute("items", itemsDeTienda);

        try {
            servicioTienda.comprar(usuario, itemId);
            redirectAttributes.addFlashAttribute("mensaje", "Compra Exitosa!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());

        }

        request.getSession().setAttribute("usuario", usuario);


        return "redirect:/ver-tienda";

    }


    @GetMapping("/ver-tienda")
    public ModelAndView verTienda(HttpServletRequest request, @ModelAttribute("mensaje") String mensaje) {
        ModelMap model = new ModelMap();
        Usuario usuarioSesion = (Usuario) request.getSession().getAttribute("usuario");
        Usuario usuario = servicioLogin.buscarUsuario(usuarioSesion.getId());
        List<Item> items = servicioTienda.obtenerItemsDeTienda();

        List<Item> itemsDeInventario = servicioTienda.obtenerItemsPorInventario(usuario.getInventario().getId());

        model.addAttribute("usuario", usuario);
        model.addAttribute("items", items);
        model.addAttribute("itemsDeInventario", itemsDeInventario);
        model.addAttribute("mensaje", mensaje == null || mensaje.isEmpty() ? "Bienvenido a la tienda!" : mensaje);

        return new ModelAndView("tienda", model);
    }
}
