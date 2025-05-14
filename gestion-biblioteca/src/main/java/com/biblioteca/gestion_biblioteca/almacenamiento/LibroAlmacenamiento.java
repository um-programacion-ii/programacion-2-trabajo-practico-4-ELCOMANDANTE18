package com.biblioteca.gestion_biblioteca.almacenamiento;

import com.biblioteca.gestion_biblioteca.Libro;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class LibroAlmacenamiento {

    private List<Libro> libros = new ArrayList<>();
    private long siguienteId = 1;

    public Libro guardar(Libro libro) {
        if (libro.getId() == null) {
            libro.setId(siguienteId++);
        } else {
            libros = libros.stream().map(l -> l.getId().equals(libro.getId()) ? libro : l).collect(Collectors.toList());
        }
        libros.add(libro);
        return libro;
    }

    public Optional<Libro> obtenerPorId(Long id) {
        return libros.stream().filter(libro -> libro.getId().equals(id)).findFirst();
    }

    public List<Libro> obtenerTodos() {
        return new ArrayList<>(libros);
    }

    public Optional<Libro> obtenerPorIsbn(String isbn) {
        return libros.stream().filter(libro -> libro.getIsbn().equals(isbn)).findFirst();
    }

    public void eliminar(Long id) {
        libros.removeIf(libro -> libro.getId().equals(id));
    }
}