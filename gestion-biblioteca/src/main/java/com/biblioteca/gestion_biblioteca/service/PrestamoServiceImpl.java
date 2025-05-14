package com.biblioteca.gestion_biblioteca.service;

import com.biblioteca.gestion_biblioteca.Libro;
import com.biblioteca.gestion_biblioteca.Prestamo;
import com.biblioteca.gestion_biblioteca.Usuario;
import com.biblioteca.gestion_biblioteca.almacenamiento.PrestamoAlmacenamiento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PrestamoServiceImpl implements PrestamoService {

    private final PrestamoAlmacenamiento prestamoAlmacenamiento;

    @Autowired
    public PrestamoServiceImpl(PrestamoAlmacenamiento prestamoAlmacenamiento) {
        this.prestamoAlmacenamiento = prestamoAlmacenamiento;
    }

    @Override
    public Prestamo realizarPrestamo(Libro libro, Usuario usuario, LocalDate fechaPrestamo, LocalDate fechaDevolucionEsperada) {
        Prestamo prestamo = new Prestamo(null, libro, usuario, fechaPrestamo, fechaDevolucionEsperada, null);
        return prestamoAlmacenamiento.guardar(prestamo);
    }

    @Override
    public Optional<Prestamo> obtenerPrestamoPorId(Long id) {
        return prestamoAlmacenamiento.obtenerPorId(id);
    }

    @Override
    public List<Prestamo> obtenerPrestamosPorUsuario(Usuario usuario) {
        return prestamoAlmacenamiento.obtenerPrestamosPorUsuario(usuario.getId());
    }

    @Override
    public List<Prestamo> obtenerPrestamosPorLibro(Libro libro) {
        return prestamoAlmacenamiento.obtenerPrestamosPorLibro(libro.getId());
    }

    @Override
    public List<Prestamo> obtenerTodosPrestamos() {
        return prestamoAlmacenamiento.obtenerTodos();
    }

    @Override
    public void marcarComoDevuelto(Long id, LocalDate fechaDevolucionReal) {
        prestamoAlmacenamiento.obtenerPorId(id).ifPresent(prestamo -> {
            prestamo.setFechaDevolucionReal(fechaDevolucionReal);
            prestamoAlmacenamiento.guardar(prestamo);
        });
    }

    @Override
    public void eliminarPrestamo(Long id) {
        prestamoAlmacenamiento.eliminar(id);
    }
}