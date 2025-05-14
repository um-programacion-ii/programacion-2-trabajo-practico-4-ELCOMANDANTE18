package com.biblioteca.gestion_biblioteca.controller;

import com.biblioteca.gestion_biblioteca.Usuario;
import com.biblioteca.gestion_biblioteca.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void obtenerTodosUsuarios_deberiaRetornarListaDeUsuariosYOk() throws Exception {
        Usuario usuario1 = new Usuario(1L, "Nombre 1", "Apellido 1", "email1@example.com");
        Usuario usuario2 = new Usuario(2L, "Nombre 2", "Apellido 2", "email2@example.com");
        List<Usuario> todosLosUsuarios = Arrays.asList(usuario1, usuario2);

        when(usuarioService.obtenerTodosUsuarios()).thenReturn(todosLosUsuarios);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/usuarios"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nombre").value("Nombre 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].email").value("email2@example.com"));

        verify(usuarioService, times(1)).obtenerTodosUsuarios();
    }

    @Test
    void obtenerUsuarioPorId_conIdExistente_deberiaRetornarUsuarioYOk() throws Exception {
        Long usuarioId = 1L;
        Usuario usuario = new Usuario(usuarioId, "Nombre", "Apellido", "email@example.com");

        when(usuarioService.obtenerUsuarioPorId(usuarioId)).thenReturn(Optional.of(usuario));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/usuarios/{id}", usuarioId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(usuarioId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Nombre"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("email@example.com"));

        verify(usuarioService, times(1)).obtenerUsuarioPorId(usuarioId);
    }

    @Test
    void obtenerUsuarioPorId_conIdNoExistente_deberiaRetornarNotFound() throws Exception {
        Long usuarioId = 1L;

        when(usuarioService.obtenerUsuarioPorId(usuarioId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/usuarios/{id}", usuarioId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(usuarioService, times(1)).obtenerUsuarioPorId(usuarioId);
    }

    @Test
    void crearUsuario_deberiaCrearUsuarioYReturnCreated() throws Exception {
        Usuario usuarioACrear = new Usuario(null, "Nuevo Nombre", "Nuevo Apellido", "nuevo@example.com");
        Usuario usuarioCreado = new Usuario(1L, "Nuevo Nombre", "Nuevo Apellido", "nuevo@example.com");

        when(usuarioService.guardarUsuario(any(Usuario.class))).thenReturn(usuarioCreado);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioACrear)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Nuevo Nombre"));

        verify(usuarioService, times(1)).guardarUsuario(any(Usuario.class));
    }

    @Test
    void actualizarUsuario_conIdExistente_deberiaActualizarUsuarioYReturnOk() throws Exception {
        Long usuarioId = 1L;
        Usuario usuarioActualizado = new Usuario(usuarioId, "Nombre Actualizado", "Apellido Actualizado", "actualizado@example.com");

        when(usuarioService.actualizarUsuario(eq(usuarioId), any(Usuario.class))).thenReturn(usuarioActualizado);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/usuarios/{id}", usuarioId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioActualizado)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(usuarioId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Nombre Actualizado"));

        verify(usuarioService, times(1)).actualizarUsuario(eq(usuarioId), any(Usuario.class));
    }

    @Test
    void actualizarUsuario_conIdNoExistente_deberiaRetornarNotFound() throws Exception {
        Long usuarioId = 1L;
        Usuario usuarioActualizado = new Usuario(usuarioId, "Nombre Actualizado", "Apellido Actualizado", "actualizado@example.com");

        when(usuarioService.actualizarUsuario(eq(usuarioId), any(Usuario.class))).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/usuarios/{id}", usuarioId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioActualizado)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(usuarioService, times(1)).actualizarUsuario(eq(usuarioId), any(Usuario.class));
    }

    @Test
    void eliminarUsuario_conIdExistente_deberiaEliminarUsuarioYReturnNoContent() throws Exception {
        Long usuarioId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/usuarios/{id}", usuarioId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(usuarioService, times(1)).eliminarUsuario(usuarioId);
    }
}