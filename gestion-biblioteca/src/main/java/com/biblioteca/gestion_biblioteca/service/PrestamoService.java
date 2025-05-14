package com.biblioteca.gestion_biblioteca.service;

import com.biblioteca.gestion_biblioteca.Libro;
import com.biblioteca.gestion_biblioteca.Prestamo;
import com.biblioteca.gestion_biblioteca.Usuario;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PrestamoService {
    Prestamo realizarPrestamo(Libro libro, Usuario usuario, LocalDate fechaPrestamo, LocalDate fechaDevolucion);
    Optional<Prestamo> obtenerPrestamoPorId(Long id);
    List<Prestamo> obtenerPrestamosPorUsuario(Usuario usuario);
    List<Prestamo> obtenerPrestamosPorLibro(Libro libro);
    List<Prestamo> obtenerTodosPrestamos();
    void marcarComoDevuelto(Long id, LocalDate fechaDevolucion);
    void eliminarPrestamo(Long id);
}