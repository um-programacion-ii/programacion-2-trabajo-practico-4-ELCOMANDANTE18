package com.biblioteca.gestion_biblioteca.service;

import com.biblioteca.gestion_biblioteca.Usuario;
import com.biblioteca.gestion_biblioteca.almacenamiento.UsuarioAlmacenamiento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioAlmacenamiento usuarioAlmacenamiento;

    @Override
    public Usuario guardarUsuario(Usuario usuario) {
        return usuarioAlmacenamiento.guardar(usuario);
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioAlmacenamiento.obtenerPorId(id);
    }

    @Override
    public Usuario obtenerUsuarioPorEmail(String email) {
        return usuarioAlmacenamiento.obtenerPorEmail(email)
                .orElse(null);
    }

    @Override
    public List<Usuario> obtenerTodosUsuarios() {
        return usuarioAlmacenamiento.obtenerTodos();
    }

    @Override
    public void eliminarUsuario(Long id) {
        usuarioAlmacenamiento.eliminar(id);
    }

    @Override
    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        Optional<Usuario> usuarioExistenteOptional = usuarioAlmacenamiento.obtenerPorId(id);

        if (usuarioExistenteOptional.isPresent()) {
            Usuario usuarioExistente = usuarioExistenteOptional.get();
            usuarioActualizado.setId(usuarioExistente.getId()); // Aseguramos usar el ID existente
            return usuarioAlmacenamiento.guardar(usuarioActualizado);
        } else {
            return null; // O lanza una excepción
        }
    }
}