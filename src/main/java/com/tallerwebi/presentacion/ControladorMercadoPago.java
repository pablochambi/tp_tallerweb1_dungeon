package com.tallerwebi.presentacion;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.net.HttpStatus;
import com.mercadopago.resources.preference.Preference;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.servicios.ServicioLogin;
import com.tallerwebi.dominio.servicios.ServicioTienda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ControladorMercadoPago {

//  https://a9ec0606f07e.ngrok-free.app -> http://localhost:8080     url de ngrok para entrar a mp
   // @Autowired
    //private RepositorioUsuario repositorioUsuario;
    private ServicioLogin servicioLogin;
    private ServicioTienda servicioTienda;
    private final String NGROK_URL = "https://a9ec0606f07e.ngrok-free.app";

    @Autowired
    public ControladorMercadoPago(ServicioLogin servicioLogin, ServicioTienda servicioTienda) {
        this.servicioLogin = servicioLogin;
        this.servicioTienda = servicioTienda;
    }

//    @GetMapping("/comprar-oro" )
//    public ModelAndView vistaComprarOro(ModelAttribute("mensaje") String mensaje) {
//       ModelMap model = new ModelMap();
//       model.addAttribute("mensaje", mensaje);
//       return new ModelAndView("comprar-oro", model);
//    }

    @GetMapping("/comprar-oro")
    public ModelAndView vistaComprarOro(@ModelAttribute("mensaje") String mensaje) {
        ModelMap model = new ModelMap();
        model.addAttribute("mensaje", mensaje);
        return new ModelAndView("comprar-oro", model);
    }

    @PostMapping("/crear-preferencia")
    public ResponseEntity<String> crearPreferencia(@RequestParam String paqueteOro, @RequestParam Integer monto, HttpServletRequest req) {
          //  System.out.println(paqueteOro);
          //  System.out.println(monto);
            MercadoPagoConfig.setAccessToken("APP_USR-8634202743458845-070119-7feb65d0cb5df81d4d0984537500b57c-2526585017");

            BigDecimal montoReal = BigDecimal.valueOf(monto);
            Usuario u = (Usuario) req.getSession().getAttribute("usuario");


            PreferenceItemRequest item = PreferenceItemRequest.builder()
                    .id(paqueteOro)
                    .title("Compra de oro")
                    .unitPrice(montoReal)
                    .quantity(1)
                    .currencyId("ARS")
                    .build();

            List<PreferenceItemRequest> items = new ArrayList<>();
            items.add(item);

            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success(NGROK_URL + "/spring/compra-exitosa?paquete=" + paqueteOro)//cambiar url para mostrar paquete
                    .failure(NGROK_URL + "/spring/compra-fallida")
                    .pending(NGROK_URL + "/spring/home")
                    .build();

            PreferenceRequest reqPref = PreferenceRequest.builder()
                    .items(items)
                    .backUrls(backUrls)
                    .autoReturn("approved")
                    .externalReference(paqueteOro)
                    .build();

            PreferenceClient client = new PreferenceClient();
            Preference response;
           // System.out.println(backUrls);
          //  System.out.println(reqPref);
        try {
           // System.out.println(reqPref);

            response = client.create(reqPref);

           // response.p
            String initPoint = response.getInitPoint();
          //  System.out.println(initPoint);

            return ResponseEntity.ok(initPoint);

        } catch (MPApiException | MPException e) {
            e.printStackTrace();
            if (e instanceof MPApiException){
            System.err.println("Código: " + ((MPApiException) e).getApiResponse());
            System.err.println("Respuesta: " + ((MPApiException) e).getApiResponse().getContent());}
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("/error");
        }


    }

    @GetMapping("/compra-exitosa")
    public ModelAndView compraExitosa(@RequestParam(name = "paquete") String paqueteOro,
                                      HttpServletRequest request,
                                      RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        if (usuario == null) {
            return new ModelAndView("redirect:/login");
        }

        servicioTienda.sumarOro(paqueteOro, usuario);

        request.getSession().setAttribute("usuario", usuario);

//        ModelMap modelo = new ModelMap();
//        modelo.addAttribute("mensaje", "Compra exitosa. Se acreditaron sus monedas de oro.");
        redirectAttributes.addFlashAttribute("mensaje", "Su compra ha sido exitosa. Se acreditaron sus monedas de oro");
        return new ModelAndView("redirect:/comprar-oro");
    }

    @GetMapping("/compra-fallida")
    public ModelAndView compraFallida(RedirectAttributes redirectAttributes) {
//        ModelMap modelo = new ModelMap();
//        modelo.addAttribute("mensaje", "La compra fue cancelada o falló.");
        redirectAttributes.addFlashAttribute("mensaje", "Su compra ha fallado.");
        return new ModelAndView("redirect:/comprar-oro");
    }

}
