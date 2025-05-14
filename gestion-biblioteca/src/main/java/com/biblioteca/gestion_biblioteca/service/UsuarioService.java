package com.biblioteca.gestion_biblioteca.service;

import com.biblioteca.gestion_biblioteca.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    Usuario guardarUsuario(Usuario usuario);
    Optional<Usuario> obtenerUsuarioPorId(Long id);
    Usuario obtenerUsuarioPorEmail(String email);
    List<Usuario> obtenerTodosUsuarios();
    void eliminarUsuario(Long id);
    Usuario actualizarUsuario(Long id, Usuario usuario);
}