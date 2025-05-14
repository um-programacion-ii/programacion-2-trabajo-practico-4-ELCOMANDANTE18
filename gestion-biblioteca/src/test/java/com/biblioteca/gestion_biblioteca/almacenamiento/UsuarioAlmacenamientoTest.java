package com.biblioteca.gestion_biblioteca.almacenamiento;

import com.biblioteca.gestion_biblioteca.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioAlmacenamientoTest {

    private UsuarioAlmacenamiento usuarioAlmacenamiento;
    private Usuario usuario1;
    private Usuario usuario2;

    @BeforeEach
    void setUp() {
        usuarioAlmacenamiento = new UsuarioAlmacenamiento();
        usuario1 = new Usuario(1L, "Nombre 1", "Apellido 1", "email1@example.com");
        usuario2 = new Usuario(2L, "Nombre 2", "Apellido 2", "email2@example.com");
        usuarioAlmacenamiento.guardar(usuario1);
        usuarioAlmacenamiento.guardar(usuario2);
    }

    @Test
    void guardar_deberiaGuardarUnUsuarioYAsignarUnIdSiEsNecesario() {
        Usuario nuevoUsuario = new Usuario(null, "Nuevo Nombre", "Nuevo Apellido", "nuevo@example.com");
        Usuario usuarioGuardado = usuarioAlmacenamiento.guardar(nuevoUsuario);
        assertNotNull(usuarioGuardado.getId());
        assertEquals("Nuevo Nombre", usuarioGuardado.getNombre());
        assertTrue(usuarioAlmacenamiento.obtenerTodos().contains(usuarioGuardado));
    }

    @Test
    void obtenerPorId_conIdExistente_deberiaRetornarElUsuario() {
        Optional<Usuario> usuarioEncontrado = usuarioAlmacenamiento.obtenerPorId(1L);
        assertTrue(usuarioEncontrado.isPresent());
        assertEquals("Nombre 1", usuarioEncontrado.get().getNombre());
    }

    @Test
    void obtenerPorId_conIdNoExistente_deberiaRetornarOptionalVacio() {
        Optional<Usuario> usuarioEncontrado = usuarioAlmacenamiento.obtenerPorId(99L);
        assertTrue(usuarioEncontrado.isEmpty());
    }

    @Test
    void obtenerTodos_deberiaRetornarTodosLosUsuarios() {
        List<Usuario> todosLosUsuarios = usuarioAlmacenamiento.obtenerTodos();
        assertEquals(2, todosLosUsuarios.size());
        assertTrue(todosLosUsuarios.contains(usuario1));
        assertTrue(todosLosUsuarios.contains(usuario2));
    }

    @Test
    void eliminar_conIdExistente_deberiaEliminarElUsuario() {
        usuarioAlmacenamiento.eliminar(1L);
        assertEquals(1, usuarioAlmacenamiento.obtenerTodos().size());
        assertFalse(usuarioAlmacenamiento.obtenerTodos().contains(usuario1));
    }

    @Test
    void obtenerPorEmail_conEmailExistente_deberiaRetornarElUsuario() {
        Optional<Usuario> usuarioEncontrado = usuarioAlmacenamiento.obtenerPorEmail("email1@example.com");
        assertTrue(usuarioEncontrado.isPresent());
        assertEquals("Nombre 1", usuarioEncontrado.get().getNombre());
    }

    @Test
    void obtenerPorEmail_conEmailNoExistente_deberiaRetornarOptionalVacio() {
        Optional<Usuario> usuarioEncontrado = usuarioAlmacenamiento.obtenerPorEmail("noexiste@example.com");
        assertTrue(usuarioEncontrado.isEmpty());
    }
}