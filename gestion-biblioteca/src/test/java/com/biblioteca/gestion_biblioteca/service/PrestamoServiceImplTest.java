package com.biblioteca.gestion_biblioteca.service;

import com.biblioteca.gestion_biblioteca.Libro;
import com.biblioteca.gestion_biblioteca.Prestamo;
import com.biblioteca.gestion_biblioteca.Usuario;
import com.biblioteca.gestion_biblioteca.almacenamiento.PrestamoAlmacenamiento;
import com.biblioteca.gestion_biblioteca.EstadoLibro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PrestamoServiceImplTest {

    @Mock
    private PrestamoAlmacenamiento prestamoAlmacenamiento;

    @InjectMocks
    private PrestamoServiceImpl prestamoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void realizarPrestamo_deberiaGuardarYRetornarPrestamo() {
        Libro libro = new Libro(1L, "123", "Título", "Autor", EstadoLibro.DISPONIBLE);
        Usuario usuario = new Usuario(1L, "Nombre", "Apellido", "email");
        LocalDate fechaPrestamo = LocalDate.now();
        LocalDate fechaDevolucionEsperada = LocalDate.now().plusDays(7);
        Prestamo prestamoAGuardar = new Prestamo(null, libro, usuario, fechaPrestamo, fechaDevolucionEsperada, null);
        Prestamo prestamoGuardado = new Prestamo(1L, libro, usuario, fechaPrestamo, fechaDevolucionEsperada, null);
        when(prestamoAlmacenamiento.guardar(any(Prestamo.class))).thenReturn(prestamoGuardado);
        when(prestamoAlmacenamiento.obtenerPorId(1L)).thenReturn(Optional.of(prestamoGuardado)); // Para posibles llamadas posteriores

        Prestamo prestamoRealizado = prestamoService.realizarPrestamo(libro, usuario, fechaPrestamo, fechaDevolucionEsperada);

        assertNotNull(prestamoRealizado);
        assertEquals(libro, prestamoRealizado.getLibro());
        assertEquals(usuario, prestamoRealizado.getUsuario());
        assertEquals(fechaPrestamo, prestamoRealizado.getFechaPrestamo());
        assertEquals(fechaDevolucionEsperada, prestamoRealizado.getFechaDevolucionEsperada());
        verify(prestamoAlmacenamiento, times(1)).guardar(any(Prestamo.class));
    }

    @Test
    void obtenerPrestamoPorId_conIdExistente_deberiaRetornarPrestamo() {
        Long prestamoId = 1L;
        Libro libro = new Libro(1L, "123", "Título", "Autor", EstadoLibro.DISPONIBLE);
        Usuario usuario = new Usuario(1L, "Nombre", "Apellido", "email");
        LocalDate fechaPrestamo = LocalDate.now();
        LocalDate fechaDevolucionEsperada = LocalDate.now().plusDays(7);
        Prestamo prestamoEncontrado = new Prestamo(prestamoId, libro, usuario, fechaPrestamo, fechaDevolucionEsperada, null);
        when(prestamoAlmacenamiento.obtenerPorId(prestamoId)).thenReturn(Optional.of(prestamoEncontrado));

        Optional<Prestamo> resultado = prestamoService.obtenerPrestamoPorId(prestamoId);

        assertTrue(resultado.isPresent());
        assertEquals(libro, resultado.get().getLibro());
        assertEquals(prestamoId, resultado.get().getId());
        verify(prestamoAlmacenamiento, times(1)).obtenerPorId(prestamoId);
    }

    @Test
    void obtenerPrestamoPorId_conIdNoExistente_deberiaRetornarOptionalVacio() {
        Long prestamoId = 1L;
        when(prestamoAlmacenamiento.obtenerPorId(prestamoId)).thenReturn(Optional.empty());

        Optional<Prestamo> resultado = prestamoService.obtenerPrestamoPorId(prestamoId);

        assertTrue(resultado.isEmpty());
        verify(prestamoAlmacenamiento, times(1)).obtenerPorId(prestamoId);
    }

    @Test
    void obtenerPrestamosPorUsuario_deberiaRetornarListaDePrestamosDelUsuario() {
        Usuario usuario1 = new Usuario(1L, "Nombre1", "Apellido1", "email1");
        Usuario usuario2 = new Usuario(2L, "Nombre2", "Apellido2", "email2");
        Libro libro1 = new Libro(1L, "123", "Título1", "Autor1", EstadoLibro.DISPONIBLE);
        Libro libro2 = new Libro(2L, "456", "Título2", "Autor2", EstadoLibro.DISPONIBLE);
        LocalDate fechaPrestamo = LocalDate.now();
        LocalDate fechaDevolucion = LocalDate.now().plusDays(7);
        List<Prestamo> prestamos = Arrays.asList(
                new Prestamo(1L, libro1, usuario1, fechaPrestamo, fechaDevolucion, null),
                new Prestamo(2L, libro2, usuario1, fechaPrestamo.plusDays(1), fechaDevolucion.plusDays(8), null),
                new Prestamo(3L, libro1, usuario2, fechaPrestamo, fechaDevolucion, null)
        );
        when(prestamoAlmacenamiento.obtenerPrestamosPorUsuario(usuario1.getId())).thenReturn(Arrays.asList(prestamos.get(0), prestamos.get(1)));

        List<Prestamo> resultado = prestamoService.obtenerPrestamosPorUsuario(usuario1);

        assertEquals(2, resultado.size());
        assertEquals(usuario1, resultado.get(0).getUsuario());
        assertEquals(usuario1, resultado.get(1).getUsuario());
        verify(prestamoAlmacenamiento, times(1)).obtenerPrestamosPorUsuario(usuario1.getId());
    }

    @Test
    void obtenerPrestamosPorLibro_deberiaRetornarListaDePrestamosDelLibro() {
        Libro libro1 = new Libro(1L, "123", "Título1", "Autor1", EstadoLibro.DISPONIBLE);
        Libro libro2 = new Libro(2L, "456", "Título2", "Autor2", EstadoLibro.DISPONIBLE);
        Usuario usuario1 = new Usuario(1L, "Nombre1", "Apellido1", "email1");
        Usuario usuario2 = new Usuario(2L, "Nombre2", "Apellido2", "email2");
        LocalDate fechaPrestamo = LocalDate.now();
        LocalDate fechaDevolucion = LocalDate.now().plusDays(7);
        List<Prestamo> prestamos = Arrays.asList(
                new Prestamo(1L, libro1, usuario1, fechaPrestamo, fechaDevolucion, null),
                new Prestamo(2L, libro1, usuario2, fechaPrestamo.plusDays(1), fechaDevolucion.plusDays(8), null),
                new Prestamo(3L, libro2, usuario1, fechaPrestamo, fechaDevolucion, null)
        );
        when(prestamoAlmacenamiento.obtenerPrestamosPorLibro(libro1.getId())).thenReturn(Arrays.asList(prestamos.get(0), prestamos.get(1)));

        List<Prestamo> resultado = prestamoService.obtenerPrestamosPorLibro(libro1);

        assertEquals(2, resultado.size());
        assertEquals(libro1, resultado.get(0).getLibro());
        assertEquals(libro1, resultado.get(1).getLibro());
        verify(prestamoAlmacenamiento, times(1)).obtenerPrestamosPorLibro(libro1.getId());
    }

    @Test
    void obtenerTodosPrestamos_deberiaRetornarListaDeTodosLosPrestamos() {
        List<Prestamo> prestamos = Arrays.asList(
                new Prestamo(1L, new Libro(), new Usuario(), LocalDate.now(), LocalDate.now().plusDays(7), null),
                new Prestamo(2L, new Libro(), new Usuario(), LocalDate.now().plusDays(1), LocalDate.now().plusDays(8), null)
        );
        when(prestamoAlmacenamiento.obtenerTodos()).thenReturn(prestamos);

        List<Prestamo> resultado = prestamoService.obtenerTodosPrestamos();

        assertEquals(2, resultado.size());
        verify(prestamoAlmacenamiento, times(1)).obtenerTodos();
    }

    @Test
    void marcarComoDevuelto_conPrestamoExistente_deberiaActualizarFechaDevolucion() {
        Long prestamoId = 1L;
        LocalDate fechaDevolucionReal = LocalDate.now();
        Prestamo prestamoExistente = new Prestamo(prestamoId, new Libro(), new Usuario(), LocalDate.now(), LocalDate.now().plusDays(7), null);
        when(prestamoAlmacenamiento.obtenerPorId(prestamoId)).thenReturn(Optional.of(prestamoExistente));
        when(prestamoAlmacenamiento.guardar(prestamoExistente)).thenReturn(prestamoExistente);

        prestamoService.marcarComoDevuelto(prestamoId, fechaDevolucionReal);

        assertEquals(fechaDevolucionReal, prestamoExistente.getFechaDevolucionReal());
        verify(prestamoAlmacenamiento, times(1)).obtenerPorId(prestamoId);
        verify(prestamoAlmacenamiento, times(1)).guardar(prestamoExistente);
    }

    @Test
    void marcarComoDevuelto_conPrestamoNoExistente_noDeberiaHacerNada() {
        Long prestamoId = 1L;
        LocalDate fechaDevolucionReal = LocalDate.now();
        when(prestamoAlmacenamiento.obtenerPorId(prestamoId)).thenReturn(Optional.empty());

        prestamoService.marcarComoDevuelto(prestamoId, fechaDevolucionReal);

        verify(prestamoAlmacenamiento, times(1)).obtenerPorId(prestamoId);
        verify(prestamoAlmacenamiento, never()).guardar(any());
    }

    @Test
    void eliminarPrestamo_deberiaLlamarAlMetodoEliminarDelAlmacenamiento() {
        Long prestamoId = 1L;
        doNothing().when(prestamoAlmacenamiento).eliminar(prestamoId);

        prestamoService.eliminarPrestamo(prestamoId);

        verify(prestamoAlmacenamiento, times(1)).eliminar(prestamoId);
    }
}