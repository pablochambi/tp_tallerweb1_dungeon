package com.tallerwebi.dominio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import javax.persistence.criteria.CriteriaBuilder;

public class ServicioReclutaTest {

    ServicioRecluta servicioRecluta = new ServicioReclutaImpl();

    @Test
    public void dadoUnaFormacionVaciaYNivelCero_devuelveDosReclutas() {
        Integer cantidadReclutas = whenObtenerCantidadReclutasSemanales(0,0);
        thenLaCantidadDeReclutasEsValida(cantidadReclutas,2);
    }

    private void thenLaCantidadDeReclutasEsValida(Integer cantidadReclutas,Integer numReclutasEsperados) {
        assertThat(cantidadReclutas, is(2));
    }

    private Integer whenObtenerCantidadReclutasSemanales(int nivelRedDeLLegadas, Integer tamanioDeFormacion) {
        Integer cantidadReclutas = servicioRecluta.getCantidadDeReclutasSemanales(nivelRedDeLLegadas,tamanioDeFormacion);
        return cantidadReclutas;
    }


}
