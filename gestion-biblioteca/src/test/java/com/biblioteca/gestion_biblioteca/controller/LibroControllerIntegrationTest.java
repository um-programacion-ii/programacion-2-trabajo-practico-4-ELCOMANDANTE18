package com.biblioteca.gestion_biblioteca.controller;

import com.biblioteca.gestion_biblioteca.Libro;
import com.biblioteca.gestion_biblioteca.EstadoLibro;
import com.biblioteca.gestion_biblioteca.service.LibroService;
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

@WebMvcTest(LibroController.class)
public class LibroControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibroService libroService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void obtenerTodosLibros_deberiaRetornarListaDeLibrosYOk() throws Exception {
        Libro libro1 = new Libro(1L, "123-456", "Título 1", "Autor 1", EstadoLibro.DISPONIBLE);
        Libro libro2 = new Libro(2L, "789-012", "Título 2", "Autor 2", EstadoLibro.PRESTADO);
        List<Libro> todosLosLibros = Arrays.asList(libro1, libro2);

        when(libroService.obtenerTodosLibros()).thenReturn(todosLosLibros);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/libros"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].titulo").value("Título 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].autor").value("Autor 2"));

        verify(libroService, times(1)).obtenerTodosLibros();
    }

    @Test
    void obtenerLibroPorId_conIdExistente_deberiaRetornarLibroYOk() throws Exception {
        Long libroId = 1L;
        Libro libro = new Libro(libroId, "123-456", "Título", "Autor", EstadoLibro.DISPONIBLE);

        when(libroService.obtenerLibroPorId(libroId)).thenReturn(Optional.of(libro));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/libros/{id}", libroId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(libroId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.titulo").value("Título"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.estado").value("DISPONIBLE"));

        verify(libroService, times(1)).obtenerLibroPorId(libroId);
    }

    @Test
    void obtenerLibroPorId_conIdNoExistente_deberiaRetornarNotFound() throws Exception {
        Long libroId = 1L;

        when(libroService.obtenerLibroPorId(libroId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/libros/{id}", libroId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(libroService, times(1)).obtenerLibroPorId(libroId);
    }

    @Test
    void crearLibro_deberiaCrearLibroYReturnCreated() throws Exception {
        Libro libroACrear = new Libro(null, "111-222", "Nuevo Título", "Nuevo Autor", EstadoLibro.DISPONIBLE);
        Libro libroCreado = new Libro(1L, "111-222", "Nuevo Título", "Nuevo Autor", EstadoLibro.DISPONIBLE);

        when(libroService.guardarLibro(any(Libro.class))).thenReturn(libroCreado);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(libroACrear)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.titulo").value("Nuevo Título"));

        verify(libroService, times(1)).guardarLibro(any(Libro.class));
    }

    @Test
    void actualizarLibro_conIdExistente_deberiaActualizarLibroYReturnOk() throws Exception {
        Long libroId = 1L;
        Libro libroActualizado = new Libro(libroId, "777-888", "Título Actualizado", "Autor Actualizado", EstadoLibro.PRESTADO);

        when(libroService.actualizarLibro(eq(libroId), any(Libro.class))).thenReturn(libroActualizado);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/libros/{id}", libroId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(libroActualizado)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(libroId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.titulo").value("Título Actualizado"));

        verify(libroService, times(1)).actualizarLibro(eq(libroId), any(Libro.class));
    }

    @Test
    void actualizarLibro_conIdNoExistente_deberiaRetornarNotFound() throws Exception {
        Long libroId = 1L;
        Libro libroActualizado = new Libro(libroId, "777-888", "Título Actualizado", "Autor Actualizado", EstadoLibro.PRESTADO);

        when(libroService.actualizarLibro(eq(libroId), any(Libro.class))).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/libros/{id}", libroId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(libroActualizado)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(libroService, times(1)).actualizarLibro(eq(libroId), any(Libro.class));
    }

    @Test
    void eliminarLibro_conIdExistente_deberiaEliminarLibroYReturnNoContent() throws Exception {
        Long libroId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/libros/{id}", libroId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(libroService, times(1)).eliminarLibro(libroId);
    }
}