package com.biblioteca.gestion_biblioteca.controller;

import com.biblioteca.gestion_biblioteca.Libro;
import com.biblioteca.gestion_biblioteca.Prestamo;
import com.biblioteca.gestion_biblioteca.Usuario;
import com.biblioteca.gestion_biblioteca.service.LibroService;
import com.biblioteca.gestion_biblioteca.service.PrestamoService;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@WebMvcTest(PrestamoController.class)
public class PrestamoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PrestamoService prestamoService;

    @MockBean
    private LibroService libroService;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void obtenerTodosPrestamos_deberiaRetornarListaDePrestamosYOk() throws Exception {
        Libro libro1 = new Libro(1L, "123-456", "Título 1", "Autor 1", null);
        Usuario usuario1 = new Usuario(1L, "Nombre 1", "Apellido 1", "email1@example.com");
        Prestamo prestamo1 = new Prestamo(1L, libro1, usuario1, LocalDate.now(), LocalDate.now().plusDays(7), null);
        List<Prestamo> todosLosPrestamos = Arrays.asList(prestamo1);

        when(prestamoService.obtenerTodosPrestamos()).thenReturn(todosLosPrestamos);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/prestamos"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1));

        verify(prestamoService, times(1)).obtenerTodosPrestamos();
    }

    @Test
    void obtenerPrestamoPorId_conIdExistente_deberiaRetornarPrestamoYOk() throws Exception {
        Long prestamoId = 1L;
        Libro libro = new Libro(1L, "123-456", "Título", "Autor", null);
        Usuario usuario = new Usuario(1L, "Nombre", "Apellido", "email@example.com");
        Prestamo prestamo = new Prestamo(prestamoId, libro, usuario, LocalDate.now(), LocalDate.now().plusDays(7), null);

        when(prestamoService.obtenerPrestamoPorId(prestamoId)).thenReturn(Optional.of(prestamo));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/prestamos/{id}", prestamoId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(prestamoId));

        verify(prestamoService, times(1)).obtenerPrestamoPorId(prestamoId);
    }

    @Test
    void obtenerPrestamoPorId_conIdNoExistente_deberiaRetornarNotFound() throws Exception {
        Long prestamoId = 1L;

        when(prestamoService.obtenerPrestamoPorId(prestamoId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/prestamos/{id}", prestamoId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(prestamoService, times(1)).obtenerPrestamoPorId(prestamoId);
    }

    @Test
    void realizarPrestamo_conLibroYUsuarioExistentes_deberiaCrearPrestamoYReturnCreated() throws Exception {
        Long libroId = 1L;
        Long usuarioId = 1L;
        LocalDate fechaPrestamo = LocalDate.now();
        LocalDate fechaDevolucion = LocalDate.now().plusDays(7);
        Libro libro = new Libro(libroId, "123-456", "Título", "Autor", null);
        Usuario usuario = new Usuario(usuarioId, "Nombre", "Apellido", "email@example.com");
        Prestamo nuevoPrestamo = new Prestamo(1L, libro, usuario, fechaPrestamo, fechaDevolucion, null);

        when(libroService.obtenerLibroPorId(libroId)).thenReturn(Optional.of(libro));
        when(usuarioService.obtenerUsuarioPorId(usuarioId)).thenReturn(Optional.of(usuario));
        when(prestamoService.realizarPrestamo(libro, usuario, fechaPrestamo, fechaDevolucion)).thenReturn(nuevoPrestamo);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/prestamos")
                        .param("libroId", libroId.toString())
                        .param("usuarioId", usuarioId.toString())
                        .param("fechaPrestamo", fechaPrestamo.toString())
                        .param("fechaDevolucion", fechaDevolucion.toString()))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));

        verify(libroService, times(1)).obtenerLibroPorId(libroId);
        verify(usuarioService, times(1)).obtenerUsuarioPorId(usuarioId);
        verify(prestamoService, times(1)).realizarPrestamo(libro, usuario, fechaPrestamo, fechaDevolucion);
    }

    @Test
    void realizarPrestamo_conLibroNoExistente_deberiaRetornarNotFound() throws Exception {
        Long libroId = 1L;
        Long usuarioId = 1L;
        LocalDate fechaPrestamo = LocalDate.now();
        LocalDate fechaDevolucion = LocalDate.now().plusDays(7);

        when(libroService.obtenerLibroPorId(libroId)).thenReturn(Optional.empty());
        when(usuarioService.obtenerUsuarioPorId(usuarioId)).thenReturn(Optional.of(new Usuario()));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/prestamos")
                        .param("libroId", libroId.toString())
                        .param("usuarioId", usuarioId.toString())
                        .param("fechaPrestamo", fechaPrestamo.toString())
                        .param("fechaDevolucion", fechaDevolucion.toString()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(libroService, times(1)).obtenerLibroPorId(libroId);
        verify(usuarioService, times(1)).obtenerUsuarioPorId(usuarioId);
        verify(prestamoService, never()).realizarPrestamo(any(), any(), any(), any());
    }

    @Test
    void marcarComoDevuelto_conPrestamoExistente_deberiaMarcarComoDevueltoYReturnOk() throws Exception {
        Long prestamoId = 1L;
        LocalDate fechaDevolucion = LocalDate.now();

        doNothing().when(prestamoService).marcarComoDevuelto(prestamoId, fechaDevolucion);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/prestamos/{id}/devolver", prestamoId)
                        .param("fechaDevolucion", fechaDevolucion.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(prestamoService, times(1)).marcarComoDevuelto(prestamoId, fechaDevolucion);
    }

    @Test
    void eliminarPrestamo_conIdExistente_deberiaEliminarPrestamoYReturnNoContent() throws Exception {
        Long prestamoId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/prestamos/{id}", prestamoId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(prestamoService, times(1)).eliminarPrestamo(prestamoId);
    }
}