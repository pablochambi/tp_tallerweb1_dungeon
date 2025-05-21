package com.tallerwebi.integracion;

import com.tallerwebi.dominio.RepositorioUsuario;
import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.ServicioLoginImpl;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.presentacion.ControladorLogin;
import com.tallerwebi.presentacion.DatosLogin;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.ModelAndView;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
public class ControladorLoginTest {

	private ControladorLogin controladorLogin;


//	@Test
//	public void loginConClaveIncorrectaMuestraError() {
//		Usuario usuario = new Usuario();
//		usuario.setEmail("admin@test.com");
//		usuario.setPassword("malapass");
//
//		ModelAndView mav = controladorLogin.validarLogin(usuario);
//
//		assertEquals("login", mav.getViewName());
//		assertEquals("Usuario o clave incorrecta", mav.getModel().get("error"));
//	}

//	@Test
//	public void loginConUsuarioInexistenteMuestraError() {
//		Usuario usuario = new Usuario();
//		usuario.setEmail("noexiste@test.com");
//		usuario.setPassword("1234");
//
//		ModelAndView mav = controladorLogin.validarLogin(usuario);
//
//		assertEquals("login", mav.getViewName());
//		assertEquals("Usuario o clave incorrecta", mav.getModel().get("error"));
//	}
//}


	//@BeforeEach
	//public void init(){
	//	usuarioMock = mock(Usuario.class);
	//	when(usuarioMock.getEmail()).thenReturn("dami@unlam.com");
	//	this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	//}
//
//	@Test
//	public void debeRetornarLaPaginaLoginCuandoSeNavegaALaRaiz() throws Exception {
//
//		MvcResult result = this.mockMvc.perform(get("/"))
//				/*.andDo(print())*/
//				.andExpect(status().is3xxRedirection())
//				.andReturn();
//
//		ModelAndView modelAndView = result.getModelAndView();
//        assert modelAndView != null;
//		assertThat("redirect:/login", equalToIgnoringCase(Objects.requireNonNull(modelAndView.getViewName())));
//		assertThat(true, is(modelAndView.getModel().isEmpty()));
//	}
//
//	@Test
//	public void debeRetornarLaPaginaLoginCuandoSeNavegaALLogin() throws Exception {
//
//		MvcResult result = this.mockMvc.perform(get("/login"))
//				.andExpect(status().isOk())
//				.andReturn();
//
//		ModelAndView modelAndView = result.getModelAndView();
//        assert modelAndView != null;
//        assertThat(modelAndView.getViewName(), equalToIgnoringCase("login"));
//		assertThat(modelAndView.getModel().get("datosLogin").toString(),  containsString("com.tallerwebi.presentacion.DatosLogin"));
//
//	}
}
