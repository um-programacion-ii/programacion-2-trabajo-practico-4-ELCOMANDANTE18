package com.biblioteca.gestion_biblioteca.service;

import com.biblioteca.gestion_biblioteca.Libro;
import java.util.List;
import java.util.Optional;

public interface LibroService {
    Libro guardarLibro(Libro libro);
    Optional<Libro> obtenerLibroPorId(Long id);
    Libro obtenerLibroPorIsbn(String isbn);
    List<Libro> obtenerTodosLibros();
    void eliminarLibro(Long id);
    Libro actualizarLibro(Long id, Libro libro);
}