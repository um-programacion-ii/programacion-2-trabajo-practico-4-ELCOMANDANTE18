package com.biblioteca.gestion_biblioteca.almacenamiento;

import com.biblioteca.gestion_biblioteca.Libro;
import com.biblioteca.gestion_biblioteca.EstadoLibro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class LibroAlmacenamientoTest {

    private LibroAlmacenamiento libroAlmacenamiento;
    private Libro libro1;
    private Libro libro2;

    @BeforeEach
    void setUp() {
        libroAlmacenamiento = new LibroAlmacenamiento();
        libro1 = new Libro(1L, "123-456", "Título 1", "Autor 1", EstadoLibro.DISPONIBLE);
        libro2 = new Libro(2L, "789-012", "Título 2", "Autor 2", EstadoLibro.PRESTADO);
        libroAlmacenamiento.guardar(libro1);
        libroAlmacenamiento.guardar(libro2);
    }

    @Test
    void guardar_deberiaGuardarUnLibroYAsignarUnIdSiEsNecesario() {
        Libro nuevoLibro = new Libro(null, "111-222", "Nuevo Título", "Nuevo Autor", EstadoLibro.DISPONIBLE);
        Libro libroGuardado = libroAlmacenamiento.guardar(nuevoLibro);
        assertNotNull(libroGuardado.getId());
        assertEquals("Nuevo Título", libroGuardado.getTitulo());
        assertTrue(libroAlmacenamiento.obtenerTodos().contains(libroGuardado));
    }

    @Test
    void obtenerPorId_conIdExistente_deberiaRetornarElLibro() {
        Optional<Libro> libroEncontrado = libroAlmacenamiento.obtenerPorId(1L);
        assertTrue(libroEncontrado.isPresent());
        assertEquals("Título 1", libroEncontrado.get().getTitulo());
    }

    @Test
    void obtenerPorId_conIdNoExistente_deberiaRetornarOptionalVacio() {
        Optional<Libro> libroEncontrado = libroAlmacenamiento.obtenerPorId(99L);
        assertTrue(libroEncontrado.isEmpty());
    }

    @Test
    void obtenerTodos_deberiaRetornarTodosLosLibros() {
        List<Libro> todosLosLibros = libroAlmacenamiento.obtenerTodos();
        assertEquals(2, todosLosLibros.size());
        assertTrue(todosLosLibros.contains(libro1));
        assertTrue(todosLosLibros.contains(libro2));
    }

    @Test
    void eliminar_conIdExistente_deberiaEliminarElLibro() {
        libroAlmacenamiento.eliminar(1L);
        assertEquals(1, libroAlmacenamiento.obtenerTodos().size());
        assertFalse(libroAlmacenamiento.obtenerTodos().contains(libro1));
    }

    @Test
    void obtenerPorIsbn_conIsbnExistente_deberiaRetornarElLibro() {
        Optional<Libro> libroEncontrado = libroAlmacenamiento.obtenerPorIsbn("123-456");
        assertTrue(libroEncontrado.isPresent());
        assertEquals("Título 1", libroEncontrado.get().getTitulo());
    }

    @Test
    void obtenerPorIsbn_conIsbnNoExistente_deberiaRetornarOptionalVacio() {
        Optional<Libro> libroEncontrado = libroAlmacenamiento.obtenerPorIsbn("999-999");
        assertTrue(libroEncontrado.isEmpty());
    }
}