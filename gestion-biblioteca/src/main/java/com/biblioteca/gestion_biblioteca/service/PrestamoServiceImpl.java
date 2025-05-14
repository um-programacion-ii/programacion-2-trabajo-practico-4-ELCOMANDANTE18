package com.biblioteca.gestion_biblioteca.service;

import com.biblioteca.gestion_biblioteca.Libro;
import com.biblioteca.gestion_biblioteca.Prestamo;
import com.biblioteca.gestion_biblioteca.Usuario;
import com.biblioteca.gestion_biblioteca.repository.PrestamoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PrestamoServiceImpl implements PrestamoService {

    private final PrestamoRepository prestamoRepository;

    @Autowired
    public PrestamoServiceImpl(PrestamoRepository prestamoRepository) {
        this.prestamoRepository = prestamoRepository;
    }

    @Override
    public Prestamo realizarPrestamo(Libro libro, Usuario usuario, LocalDate fechaPrestamo, LocalDate fechaDevolucion) {
        Prestamo prestamo = new Prestamo(libro, usuario, fechaPrestamo, fechaDevolucion);
        return prestamoRepository.save(prestamo);
    }

    @Override
    public Optional<Prestamo> obtenerPrestamoPorId(Long id) {
        return prestamoRepository.findById(id);
    }

    @Override
    public List<Prestamo> obtenerPrestamosPorUsuario(Usuario usuario) {
        return prestamoRepository.findByUsuario(usuario);
    }

    @Override
    public List<Prestamo> obtenerPrestamosPorLibro(Libro libro) {
        return prestamoRepository.findByLibro(libro);
    }

    @Override
    public List<Prestamo> obtenerTodosPrestamos() {
        return prestamoRepository.findAll();
    }

    @Override
    public void marcarComoDevuelto(Long id, LocalDate fechaDevolucion) {
        Optional<Prestamo> prestamoExistente = prestamoRepository.findById(id);
        prestamoExistente.ifPresent(prestamo -> {
            prestamo.setFechaDevolucion(fechaDevolucion);
            prestamoRepository.save(prestamo);
        });
    }

    @Override
    public void eliminarPrestamo(Long id) {
        prestamoRepository.deleteById(id);
    }
}