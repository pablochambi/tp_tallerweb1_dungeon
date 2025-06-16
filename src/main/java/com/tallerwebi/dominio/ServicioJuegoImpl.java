package com.tallerwebi.dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.tallerwebi.dominio.entidades.*;
import com.tallerwebi.dominio.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServicioJuegoImpl implements ServicioJuego {

    @Autowired private RepositorioUsuario UsuarioRepo;
    @Autowired private RepositorioSession sessionRepo;
    @Autowired private RepositorioMonster monsterRepo;
    @Autowired private RepositorioSessionMonster smRepo;

   //recupera sesion activa, sino crea una nueva
   @Override
   public GameSession iniciarPartida() {
       GameSession session = sessionRepo.findActive();
       if (session == null) {
           session = crearNuevaMazmorra();
       }
       return session;
   }
    @Override
    public GameSession crearNuevaMazmorra() {
        // User fijo con id 1 (se crea si no existe)

        Usuario u = UsuarioRepo.buscarUsuarioPorId(1L);
        if (u == null) {
            u = new Usuario();
            u.setVida(100);
            u.setAtk(10);
            u.setDefensa(false);
            u.setOro(1000);
            UsuarioRepo.guardar(u);
        }

        GameSession session = new GameSession();
        session.setUsuario(u);
        sessionRepo.save(session);

        // agregar 3 monstruos random
        List<Monster> all = new ArrayList<>(monsterRepo.obtenerTodosLosMonstruos());
        Collections.shuffle(all);
        int toSeed = Math.min(3, all.size());
        for (int i = 0; i < toSeed; i++) {
            smRepo.add(session, all.get(i));
        }
        return session;
    }

    @Override
    public void reiniciarMazmorra() {
        GameSession session = sessionRepo.findActive();
        if (session == null) {
            // si no existe la creamos
            crearNuevaMazmorra();
        } else {
            // eliminar monstruos viejos
            smRepo.deleteBySession(session);
            // agregar monstruos 3 nuevos
            List<Monster> all = new ArrayList<>(monsterRepo.obtenerTodosLosMonstruos());
            Collections.shuffle(all);
            int toSeed = Math.min(3, all.size());
            for (int i = 0; i < toSeed; i++) {
                smRepo.add(session, all.get(i));
            }
        }
    }


    @Override
    public Usuario getUsuario() {
        return iniciarPartida().getUsuario();
    }

    @Override
    public List<SessionMonster> getMonstruos() {
        return smRepo.findBySession(iniciarPartida());
    }

    @Override
    public String atacar(int orden) {
        GameSession session = iniciarPartida();
        Usuario usuario = session.getUsuario();

        // 1) Atacar al monstruo objetivo
        SessionMonster objetivo = getMonstruos().stream()
                .filter(sm -> sm.getOrden() == orden)
                .findFirst()
                .orElse(null);
        if (objetivo == null) {
            return "Monstruo no encontrado.";
        }
        objetivo.setVidaActual(objetivo.getVidaActual() - usuario.getAtk());
        smRepo.update(objetivo);

        // 2) Monstruos vivos contraatacan y registramos sus nombres
        List<SessionMonster> vivos = getMonstruos().stream()
                .filter(sm -> sm.getVidaActual() > 0)
                .collect(Collectors.toList());

        List<String> atacantes = new ArrayList<>();
        for (SessionMonster sm : vivos) {
            atacantes.add(sm.getMonster().getNombre());
            int dano = sm.getMonster().getAtk();
            if (usuario.isDefensa()) {
                dano /= 2;
                usuario.setDefensa(false);
            }
            usuario.setVida(usuario.getVida() - dano);
        }

        UsuarioRepo.guardar(usuario);

        // 3) Construimos el texto de quién atacó
        String textoAtacantes;
        if (atacantes.isEmpty()) {
            textoAtacantes = "Nadie";
        } else if (atacantes.size() == 1) {
            textoAtacantes = atacantes.get(0) + " te contraataca";
        } else {
            // Unir con comas y "y" antes del último
            String lista = String.join(", ", atacantes.subList(0, atacantes.size()-1))
                    + " y " + atacantes.get(atacantes.size()-1);
            textoAtacantes = lista + " te contraatacan";
        }

        // 4) Devolver mensaje final
        return String.format(
                "Has atacado. %s y ahora tu vida es %d.",
                textoAtacantes,
                usuario.getVida()
        );
    }

    @Override
    public String defender() {
        GameSession session = iniciarPartida();
        Usuario usuario = session.getUsuario();

        usuario.setDefensa(true);
        UsuarioRepo.guardar(usuario);

        for (SessionMonster sm : getMonstruos()) {
            if (sm.getVidaActual() > 0) {
                int dano = sm.getMonster().getAtk() / 2;
                usuario.setVida(usuario.getVida() - dano);
            }
        }
        UsuarioRepo.guardar(usuario);
        return "Defiendes este turno. Vida restante: " + usuario.getVida() + ".";
    }

    @Override
    public String usarPocion() {
        GameSession session = iniciarPartida();
        Usuario usuario = session.getUsuario();

        usuario.setVida(usuario.getVida() + 30);
        UsuarioRepo.guardar(usuario);
        return "Usaste poción. Vida actual: " + usuario.getVida() + ".";
    }

    @Override
    public GameSession getSession() {
        return iniciarPartida();
    }

    @Override
    public void endSession(GameSession current) {
        if (current != null) {
            sessionRepo.delete(current);
        }
    }
}
