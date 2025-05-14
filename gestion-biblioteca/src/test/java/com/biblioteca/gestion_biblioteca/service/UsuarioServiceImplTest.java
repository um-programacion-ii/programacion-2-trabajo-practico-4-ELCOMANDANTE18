package com.biblioteca.gestion_biblioteca.service;

import com.biblioteca.gestion_biblioteca.Usuario;
import com.biblioteca.gestion_biblioteca.almacenamiento.UsuarioAlmacenamiento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceImplTest {

    @Mock
    private UsuarioAlmacenamiento usuarioAlmacenamiento;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void guardarUsuario_deberiaGuardarYRetornarUsuario() {
        Usuario usuarioAGuardar = new Usuario(null, "Nombre", "Apellido", "email@example.com");
        Usuario usuarioGuardado = new Usuario(1L, "Nombre", "Apellido", "email@example.com");
        when(usuarioAlmacenamiento.guardar(usuarioAGuardar)).thenReturn(usuarioGuardado);

        Usuario resultado = usuarioService.guardarUsuario(usuarioAGuardar);

        assertNotNull(resultado);
        assertEquals("Nombre", resultado.getNombre());
        verify(usuarioAlmacenamiento, times(1)).guardar(usuarioAGuardar);
    }

    @Test
    void obtenerUsuarioPorId_conIdExistente_deberiaRetornarUsuario() {
        Long usuarioId = 1L;
        Usuario usuarioEncontrado = new Usuario(usuarioId, "Nombre", "Apellido", "email@example.com");
        when(usuarioAlmacenamiento.obtenerPorId(usuarioId)).thenReturn(Optional.of(usuarioEncontrado));

        Optional<Usuario> resultado = usuarioService.obtenerUsuarioPorId(usuarioId);

        assertTrue(resultado.isPresent());
        assertEquals("email@example.com", resultado.get().getEmail());
        verify(usuarioAlmacenamiento, times(1)).obtenerPorId(usuarioId);
    }

    @Test
    void obtenerUsuarioPorId_conIdNoExistente_deberiaRetornarOptionalVacio() {
        Long usuarioId = 1L;
        when(usuarioAlmacenamiento.obtenerPorId(usuarioId)).thenReturn(Optional.empty());

        Optional<Usuario> resultado = usuarioService.obtenerUsuarioPorId(usuarioId);

        assertTrue(resultado.isEmpty());
        verify(usuarioAlmacenamiento, times(1)).obtenerPorId(usuarioId);
    }

    @Test
    void obtenerTodosUsuarios_deberiaRetornarListaDeUsuarios() {
        List<Usuario> usuarios = Arrays.asList(
                new Usuario(1L, "Nombre 1", "Apellido 1", "email1@example.com"),
                new Usuario(2L, "Nombre 2", "Apellido 2", "email2@example.com")
        );
        when(usuarioAlmacenamiento.obtenerTodos()).thenReturn(usuarios);

        List<Usuario> resultado = usuarioService.obtenerTodosUsuarios();

        assertEquals(2, resultado.size());
        assertEquals("Nombre 1", resultado.get(0).getNombre());
        verify(usuarioAlmacenamiento, times(1)).obtenerTodos();
    }

    @Test
    void eliminarUsuario_deberiaLlamarAlMetodoEliminarDelAlmacenamiento() {
        Long usuarioId = 1L;
        doNothing().when(usuarioAlmacenamiento).eliminar(usuarioId);

        usuarioService.eliminarUsuario(usuarioId);

        verify(usuarioAlmacenamiento, times(1)).eliminar(usuarioId);
    }

    @Test
    void actualizarUsuario_conIdExistente_deberiaActualizarYRetornarUsuarioActualizado() {
        Long usuarioId = 1L;
        Usuario usuarioExistente = new Usuario(usuarioId, "Nombre Original", "Apellido Original", "original@example.com");
        Usuario usuarioActualizado = new Usuario(usuarioId, "Nombre Actualizado", "Apellido Actualizado", "actualizado@example.com");

        when(usuarioAlmacenamiento.obtenerPorId(usuarioId)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioAlmacenamiento.guardar(usuarioActualizado)).thenReturn(usuarioActualizado);

        Usuario resultado = usuarioService.actualizarUsuario(usuarioId, usuarioActualizado);

        assertNotNull(resultado);
        assertEquals("Nombre Actualizado", resultado.getNombre());
        verify(usuarioAlmacenamiento, times(1)).obtenerPorId(usuarioId);
        verify(usuarioAlmacenamiento, times(1)).guardar(usuarioActualizado);
    }

    @Test
    void actualizarUsuario_conIdNoExistente_deberiaFuncionarSinFallar() {
        Long usuarioId = 1L;
        Usuario usuarioActualizado = new Usuario(usuarioId, "Nombre Actualizado", "Apellido Actualizado", "actualizado@example.com");

        when(usuarioAlmacenamiento.obtenerPorId(usuarioId)).thenReturn(Optional.empty());
        // No necesitamos configurar el comportamiento de guardar aquí

        usuarioService.actualizarUsuario(usuarioId, usuarioActualizado);

        verify(usuarioAlmacenamiento, times(1)).obtenerPorId(usuarioId);
        // NO verificamos la llamada a guardar en este caso
    }

    @Test
    void obtenerUsuarioPorEmail_conEmailExistente_deberiaRetornarUsuario() {
        String email = "test@example.com";
        Usuario usuarioEncontrado = new Usuario(1L, "Nombre", "Apellido", email);
        when(usuarioAlmacenamiento.obtenerPorEmail(email)).thenReturn(Optional.of(usuarioEncontrado));

        Usuario resultado = usuarioService.obtenerUsuarioPorEmail(email);

        assertNotNull(resultado);
        assertEquals("Nombre", resultado.getNombre());
        verify(usuarioAlmacenamiento, times(1)).obtenerPorEmail(email);
    }

    @Test
    void obtenerUsuarioPorEmail_conEmailNoExistente_deberiaRetornarNull() {
        String email = "test@example.com";
        when(usuarioAlmacenamiento.obtenerPorEmail(email)).thenReturn(Optional.empty());

        Usuario resultado = usuarioService.obtenerUsuarioPorEmail(email);

        assertNull(resultado);
        verify(usuarioAlmacenamiento, times(1)).obtenerPorEmail(email);
    }
}