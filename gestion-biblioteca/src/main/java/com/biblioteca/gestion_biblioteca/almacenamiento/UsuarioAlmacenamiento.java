package com.biblioteca.gestion_biblioteca.almacenamiento;

import com.biblioteca.gestion_biblioteca.Usuario;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UsuarioAlmacenamiento {

    private List<Usuario> usuarios = new ArrayList<>();
    private long siguienteId = 1;

    public Usuario guardar(Usuario usuario) {
        if (usuario.getId() == null) {
            usuario.setId(siguienteId++);
        } else {
            usuarios = usuarios.stream().map(u -> u.getId().equals(usuario.getId()) ? usuario : u).collect(Collectors.toList());
        }
        usuarios.add(usuario);
        return usuario;
    }

    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarios.stream().filter(usuario -> usuario.getId().equals(id)).findFirst();
    }

    public List<Usuario> obtenerTodos() {
        return new ArrayList<>(usuarios);
    }

    public Optional<Usuario> obtenerPorEmail(String email) {
        return usuarios.stream().filter(usuario -> usuario.getEmail().equals(email)).findFirst();
    }

    public void eliminar(Long id) {
        usuarios.removeIf(usuario -> usuario.getId().equals(id));
    }

    // Puedes añadir más métodos según necesites (buscar por nombre, etc.)
}