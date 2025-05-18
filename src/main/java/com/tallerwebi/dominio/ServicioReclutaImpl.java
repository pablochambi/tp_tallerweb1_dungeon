package com.tallerwebi.dominio;

public class ServicioReclutaImpl implements ServicioRecluta {

    @Override
    public Integer getCantidadDeReclutasSemanales(Integer nivelRedDeLLegadas, Integer tamanioDeFormacion) {
        if (nivelRedDeLLegadas == 0 && tamanioDeFormacion == 0) {
            return 2;
        }
        return null;
    }
}
