package com.biblioteca.gestion_biblioteca.almacenamiento;

import com.biblioteca.gestion_biblioteca.Prestamo;
import com.biblioteca.gestion_biblioteca.Libro;
import com.biblioteca.gestion_biblioteca.Usuario;
import com.biblioteca.gestion_biblioteca.EstadoLibro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class PrestamoAlmacenamientoTest {

    private PrestamoAlmacenamiento prestamoAlmacenamiento;
    private Usuario usuario1;
    private Libro libro1;
    private Prestamo prestamo1;
    private Prestamo prestamo2;

    @BeforeEach
    void setUp() {
        prestamoAlmacenamiento = new PrestamoAlmacenamiento();
        usuario1 = new Usuario(1L, "Nombre 1", "Apellido 1", "email1@example.com");
        libro1 = new Libro(1L, "123-456", "Título 1", "Autor 1", EstadoLibro.DISPONIBLE);
        prestamo1 = new Prestamo(1L, libro1, usuario1, LocalDate.now(), LocalDate.now().plusDays(7), null); // Añadimos null para fechaDevolucionReal
        prestamo2 = new Prestamo(2L, new Libro(2L, "789-012", "Título 2", "Autor 2", EstadoLibro.DISPONIBLE),
                new Usuario(2L, "Nombre 2", "Apellido 2", "email2@example.com"), LocalDate.now().minusDays(2),
                LocalDate.now().plusDays(5), null); // Añadimos null para fechaDevolucionReal
        prestamoAlmacenamiento.guardar(prestamo1);
        prestamoAlmacenamiento.guardar(prestamo2);
    }

    @Test
    void guardar_deberiaGuardarUnPrestamoYAsignarUnIdSiEsNecesario() {
        Libro nuevoLibro = new Libro(3L, "111-222", "Nuevo Título", "Nuevo Autor", EstadoLibro.DISPONIBLE);
        Usuario nuevoUsuario = new Usuario(3L, "Nuevo Nombre", "Nuevo Apellido", "nuevo@example.com");
        Prestamo nuevoPrestamo = new Prestamo(null, nuevoLibro, nuevoUsuario, LocalDate.now(), LocalDate.now().plusDays(10), null); // Añadimos null
        Prestamo prestamoGuardado = prestamoAlmacenamiento.guardar(nuevoPrestamo);
        assertNotNull(prestamoGuardado.getId());
        assertEquals(nuevoLibro.getId(), prestamoGuardado.getLibro().getId());
        assertTrue(prestamoAlmacenamiento.obtenerTodos().contains(prestamoGuardado));
    }

    @Test
    void obtenerPorId_conIdExistente_deberiaRetornarElPrestamo() {
        Optional<Prestamo> prestamoEncontrado = prestamoAlmacenamiento.obtenerPorId(1L);
        assertTrue(prestamoEncontrado.isPresent());
        assertEquals(libro1.getId(), prestamoEncontrado.get().getLibro().getId());
    }

    @Test
    void obtenerPorId_conIdNoExistente_deberiaRetornarOptionalVacio() {
        Optional<Prestamo> prestamoEncontrado = prestamoAlmacenamiento.obtenerPorId(99L);
        assertTrue(prestamoEncontrado.isEmpty());
    }

    @Test
    void obtenerTodos_deberiaRetornarTodosLosPrestamos() {
        List<Prestamo> todosLosPrestamos = prestamoAlmacenamiento.obtenerTodos();
        assertEquals(2, todosLosPrestamos.size());
        assertTrue(todosLosPrestamos.contains(prestamo1));
        assertTrue(todosLosPrestamos.contains(prestamo2));
    }

    @Test
    void eliminar_conIdExistente_deberiaEliminarElPrestamo() {
        prestamoAlmacenamiento.eliminar(1L);
        assertEquals(1, prestamoAlmacenamiento.obtenerTodos().size());
        assertFalse(prestamoAlmacenamiento.obtenerTodos().contains(prestamo1));
    }

    // Aquí puedes añadir más tests específicos para tu lógica de préstamos,
    // como obtener préstamos por usuario o por libro si tienes esos métodos.
}