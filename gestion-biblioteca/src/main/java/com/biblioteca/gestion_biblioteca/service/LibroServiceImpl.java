package com.biblioteca.gestion_biblioteca.service;

import com.biblioteca.gestion_biblioteca.Libro;
import com.biblioteca.gestion_biblioteca.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LibroServiceImpl implements LibroService {

    private final LibroRepository libroRepository;

    @Autowired
    public LibroServiceImpl(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @Override
    public Libro guardarLibro(Libro libro) {
        return libroRepository.save(libro);
    }

    @Override
    public Optional<Libro> obtenerLibroPorId(Long id) {
        return libroRepository.findById(id);
    }

    @Override
    public Libro obtenerLibroPorIsbn(String isbn) {
        return libroRepository.findByIsbn(isbn);
    }

    @Override
    public List<Libro> obtenerTodosLibros() {
        return libroRepository.findAll();
    }
    @Override
    public void eliminarLibro(Long id) {
        libroRepository.deleteById(id);
    }
    @Override
    public Libro actualizarLibro(Long id, Libro libro) {
        Optional<Libro> libroExistente = libroRepository.findById(id);
        if (libroExistente.isPresent()) {
            libro.setId(id); // Aseguramos que el ID sea el correcto para la actualización
            return libroRepository.save(libro);
        }
        return null; // O lanza una excepción indicando que el libro no existe
    }
}