package com.biblioteca.gestion_biblioteca.service;

import com.biblioteca.gestion_biblioteca.Libro;
import com.biblioteca.gestion_biblioteca.almacenamiento.LibroAlmacenamiento;
import com.biblioteca.gestion_biblioteca.EstadoLibro;
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

class LibroServiceImplTest {

    @Mock
    private LibroAlmacenamiento libroAlmacenamiento;

    @InjectMocks
    private LibroServiceImpl libroService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void guardarLibro_deberiaGuardarYRetornarLibro() {
        Libro libroAGuardar = new Libro(null, "123-456", "Título", "Autor", EstadoLibro.DISPONIBLE);
        Libro libroGuardado = new Libro(1L, "123-456", "Título", "Autor", EstadoLibro.DISPONIBLE);
        when(libroAlmacenamiento.guardar(libroAGuardar)).thenReturn(libroGuardado);

        Libro resultado = libroService.guardarLibro(libroAGuardar);

        assertNotNull(resultado);
        assertEquals("123-456", resultado.getIsbn());
        verify(libroAlmacenamiento, times(1)).guardar(libroAGuardar);
    }

    @Test
    void obtenerLibroPorId_conIdExistente_deberiaRetornarLibro() {
        Long libroId = 1L;
        Libro libroEncontrado = new Libro(libroId, "123-456", "Título", "Autor", EstadoLibro.DISPONIBLE);
        when(libroAlmacenamiento.obtenerPorId(libroId)).thenReturn(Optional.of(libroEncontrado));

        Optional<Libro> resultado = libroService.obtenerLibroPorId(libroId);

        assertTrue(resultado.isPresent());
        assertEquals("Título", resultado.get().getTitulo());
        verify(libroAlmacenamiento, times(1)).obtenerPorId(libroId);
    }

    @Test
    void obtenerLibroPorId_conIdNoExistente_deberiaRetornarOptionalVacio() {
        Long libroId = 1L;
        when(libroAlmacenamiento.obtenerPorId(libroId)).thenReturn(Optional.empty());

        Optional<Libro> resultado = libroService.obtenerLibroPorId(libroId);

        assertTrue(resultado.isEmpty());
        verify(libroAlmacenamiento, times(1)).obtenerPorId(libroId);
    }

    @Test
    void obtenerTodosLibros_deberiaRetornarListaDeLibros() {
        List<Libro> libros = Arrays.asList(
                new Libro(1L, "123-456", "Título 1", "Autor 1", EstadoLibro.DISPONIBLE),
                new Libro(2L, "789-012", "Título 2", "Autor 2", EstadoLibro.PRESTADO)
        );
        when(libroAlmacenamiento.obtenerTodos()).thenReturn(libros);

        List<Libro> resultado = libroService.obtenerTodosLibros();

        assertEquals(2, resultado.size());
        assertEquals("Título 1", resultado.get(0).getTitulo());
        verify(libroAlmacenamiento, times(1)).obtenerTodos();
    }

    @Test
    void eliminarLibro_deberiaLlamarAlMetodoEliminarDelAlmacenamiento() {
        Long libroId = 1L;
        doNothing().when(libroAlmacenamiento).eliminar(libroId);

        libroService.eliminarLibro(libroId);

        verify(libroAlmacenamiento, times(1)).eliminar(libroId);
    }

    @Test
    void actualizarLibro_conIdExistente_deberiaActualizarYRetornarLibroActualizado() {
        Long libroId = 1L;
        Libro libroExistente = new Libro(libroId, "123-456", "Título Original", "Autor", EstadoLibro.DISPONIBLE);
        Libro libroActualizado = new Libro(libroId, "789-012", "Título Actualizado", "Otro Autor", EstadoLibro.PRESTADO);

        when(libroAlmacenamiento.obtenerPorId(libroId)).thenReturn(Optional.of(libroExistente));
        when(libroAlmacenamiento.guardar(libroActualizado)).thenReturn(libroActualizado);

        Libro resultado = libroService.actualizarLibro(libroId, libroActualizado);

        assertNotNull(resultado);
        assertEquals("Título Actualizado", resultado.getTitulo());
        verify(libroAlmacenamiento, times(1)).obtenerPorId(libroId);
        verify(libroAlmacenamiento, times(1)).guardar(libroActualizado);
    }

    @Test
    void actualizarLibro_conIdNoExistente_deberiaFuncionarSinFallar( ) {
        Long libroId = 1L;
        Libro libroActualizado = new Libro(libroId, "789-012", "Título Actualizado", "Otro Autor", EstadoLibro.PRESTADO);

        when(libroAlmacenamiento.obtenerPorId(libroId)).thenReturn(Optional.empty());
        // No necesitamos configurar el comportamiento de guardar aquí para este escenario

        libroService.actualizarLibro(libroId, libroActualizado);

        verify(libroAlmacenamiento, times(1)).obtenerPorId(libroId);
        // NO verificamos la llamada a guardar en este caso
    }

    /*
    @Test
    void obtenerLibroPorIsbn_conIsbnExistente_deberiaRetornarLibro() {
        String isbn = "123-456";
        Libro libroEncontrado = new Libro(1L, isbn, "Título", "Autor", EstadoLibro.DISPONIBLE);
        Optional<Libro> optionalLibroEncontrado = Optional.of(libroEncontrado); // Explicitly create Optional
        when(libroAlmacenamiento.obtenerPorIsbn(eq(isbn))).thenReturn(optionalLibroEncontrado);

        Optional<Libro> resultado = libroService.obtenerLibroPorIsbn(isbn);

        assertTrue(resultado.isPresent());
        assertEquals("Título", resultado.get().getTitulo());
        verify(libroAlmacenamiento, times(1)).obtenerPorIsbn(eq(isbn));
    }

    @Test
    void obtenerLibroPorIsbn_conIsbnNoExistente_deberiaRetornarOptionalVacio() {
        String isbn = "789-012";
        Optional<Libro> optionalLibroVacio = Optional.empty(); // Explicitly create Optional
        when(libroAlmacenamiento.obtenerPorIsbn(eq(isbn))).thenReturn(optionalLibroVacio);

        Optional<Libro> resultado = libroService.obtenerLibroPorIsbn(isbn);

        assertTrue(resultado.isEmpty());
        verify(libroAlmacenamiento, times(1)).obtenerPorIsbn(eq(isbn));
    }
    */
}