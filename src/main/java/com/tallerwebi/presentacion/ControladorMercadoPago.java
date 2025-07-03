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
import com.mercadopago.resources.preference.PreferenceBackUrls;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.interfaces.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ControladorMercadoPago {


    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @GetMapping("/comprar-oro")
    public ModelAndView vistaComprarOro() {
        return new ModelAndView("comprar-oro");
    }

//    @PostMapping("/crear-preferencia")
//    public ModelAndView crearPreferencia(@RequestParam Integer monto, HttpServletRequest request) throws MPException, MPApiException {
//        MercadoPagoConfig.setAccessToken("APP_USR-8634202743458845-070119-7feb65d0cb5df81d4d0984537500b57c-2526585017");
//
//        BigDecimal montoReal = new BigDecimal(monto);
//
//        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
//
//        PreferenceClient client = new PreferenceClient();
//        PreferenceRequest preferenceRequest = null;
//        Preference preference = client.create(preferenceRequest);
//        PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
//                .title("Compra de oro")
//                .quantity(1)
//                .currencyId("ARS")
//                .unitPrice(montoReal).build();
//
//        List<PreferenceItemRequest> items = new ArrayList<>();
//        items.add(itemRequest);
//
//
//        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
//                .success("http://localhost:8080/spring/compra-exitosa?monto=" + montoReal)
//                .failure("http://localhost:8080/spring/compra-fallida")
//                .build();
//
//
//        preferenceRequest = PreferenceRequest.builder()
//                .items(items)
//                .backUrls(backUrls)
//                .autoReturn("approved")
//                // .externalReference(monto)
//                .build();
//
//
//        //preference.save();
//
//
//        ModelMap modelo = new ModelMap();
//        modelo.addAttribute("init_point", preference.getInitPoint());
//        return new ModelAndView("redirect:" + preference.getInitPoint());
//    }

    @PostMapping("/crear-preferencia")
    public ResponseEntity<String> crearPreferencia(@RequestParam String paqueteOro, @RequestParam Integer monto, HttpServletRequest req) {
            System.out.println(paqueteOro);
            System.out.println(monto);
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
                    .success("http://localhost:8080/spring/compra-exitosa?monto=" + monto)
                    .failure("http://localhost:8080/spring/compra-fallida")
                    .pending("http://localhost:8080/spring/home")
                    .build();

            PreferenceRequest reqPref = PreferenceRequest.builder()
                    .items(items)
                    .backUrls(backUrls)
                   // .autoReturn("approved")
                    .externalReference(paqueteOro)
                    .build();

            PreferenceClient client = new PreferenceClient();
            Preference response;
            System.out.println(backUrls);
            System.out.println(reqPref);
        try {
            System.out.println(reqPref);
            response = client.create(reqPref);
           // response.p
            String initPoint = response.getInitPoint();
            System.out.println(initPoint);

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
    public ModelAndView compraExitosa(@RequestParam Integer monto, HttpServletRequest request) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Usuario usuarioDb= repositorioUsuario.buscarUsuarioPorId(usuario.getId());
        usuarioDb.setOro(usuarioDb.getOro() + monto);
        //usuario.setOro(usuario.getOro() + monto);
        repositorioUsuario.modificar(usuario);
        request.getSession().setAttribute("usuario", usuario);

        ModelMap modelo = new ModelMap();
        modelo.addAttribute("mensaje", "Compra exitosa. Se acreditaron " + monto + " de oro.");
        return new ModelAndView("redirect:/tienda", modelo);
    }

    @GetMapping("/compra-fallida")
    public ModelAndView compraFallida() {
        ModelMap modelo = new ModelMap();
        modelo.addAttribute("mensaje", "La compra fue cancelada o falló.");
        return new ModelAndView("redirect:/tienda", modelo);
    }

//    @RestController
//    public class ControladorMercadoPago {
//
//        private ServicioJugador servicioJugador;
//        private ServicioCalificacion servicioCalificacion;
//        private WhatsappNotificationService whatsappNotificationService;
//        private ServicioMail servicioMail;
//        @Autowired
//        public ControladorMercadoPago(ServicioJugador servicioJugador,
//                                      ServicioCalificacion servicioCalificacion,
//                                      ServicioNotificacion servicioNotificacion,
//                                      WhatsappNotificationService whatsappNotificationService,ServicioMail servicioMail) {
//            this.servicioJugador = servicioJugador;
//            this.servicioCalificacion = servicioCalificacion;
//            this.whatsappNotificationService = whatsappNotificationService;
//            this.servicioMail = servicioMail;
//        }
//
//        @Value("${mercadoPago.accessToken}")
//        private String mercadoPagoAccessToken;
//
//        @PostMapping("/actualizarTipoSuscripcion")
//        public void  pagarSuscripcion(
//                @RequestParam("tipoSuscripcion") String tipoSuscripcion,
//                HttpSession session,
//                HttpServletResponse response) throws Exception {
//
//            Jugador jugador = (Jugador) session.getAttribute("jugador");
//            if (jugador == null) {
//                response.sendRedirect("/login");
//                return;
//            }
//            session.setAttribute("nuevoTipoSuscripcion", tipoSuscripcion);
//
//            try {
//                System.out.println("AccessToken: " + mercadoPagoAccessToken);
//                MercadoPagoConfig.setAccessToken(mercadoPagoAccessToken);
//
//                double precio = 0;
//                switch (tipoSuscripcion) {
//                    case "Basico": precio = 1000.0; break;
//                    case "Premium_Low_Cost": precio = 1500.0; break;
//                    case "Premium_Platino": precio = 2500.0; break;
//                }
//
//                PreferenceItemRequest item = PreferenceItemRequest.builder()
//                        .title("Suscripción " + tipoSuscripcion)
//                        .quantity(1)
//                        .currencyId("ARS")
//                        .unitPrice(BigDecimal.valueOf(precio))
//                        .build();
//
//                List<PreferenceItemRequest> items = new ArrayList<>();
//                items.add(item);
//
//                PreferencePayerRequest payer = PreferencePayerRequest.builder()
//                        .name(jugador.getNombre())
//                        .surname(jugador.getAlias())
//                        .email(jugador.getEmail())
//                        .build();
//
//
//                PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
//                        .success("http://localhost:8080/success")
//                        .failure("http://localhost:8080/failure")
//                        .pending("http://localhost:8080/pending")
//                        .build();
//
//
//                PreferenceRequest preferenceRequest = PreferenceRequest.builder()
//                        .items(items)
//                        .backUrls(backUrls)
//                        .payer(payer)
//                        .build();
//
//                System.out.println("PreferenceRequest: " + preferenceRequest.toString());
//                ObjectMapper mapper = new ObjectMapper();
//                String json = mapper.writeValueAsString(preferenceRequest);
//                System.out.println("PreferenceRequest JSON: " + json);
//                PreferenceClient client = new PreferenceClient();
//                Preference preference = client.create(preferenceRequest);
//
//                response.sendRedirect(preference.getSandboxInitPoint());
//            } catch (MPApiException e) {
//                // Mostrá el error real de Mercado Pago en consola
//                System.out.println("Error de Mercado Pago: " + e.getApiResponse().getContent());
//                response.sendRedirect("/irAVerTiposDeSuscripcion?error=mp");}
}
