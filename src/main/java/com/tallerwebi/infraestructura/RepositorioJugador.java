package com.tallerwebi.infraestructura;
import org.springframework.data.repository.CrudRepository;
import com.tallerwebi.dominio.Jugador;

public interface RepositorioJugador extends CrudRepository<Jugador, Long> {
    Jugador findByNombre(String nombre);
}
