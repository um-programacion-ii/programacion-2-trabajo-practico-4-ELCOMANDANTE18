package com.biblioteca.gestion_biblioteca.service;

import com.biblioteca.gestion_biblioteca.Libro;
import com.biblioteca.gestion_biblioteca.almacenamiento.LibroAlmacenamiento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LibroServiceImpl implements LibroService {

    @Autowired
    private LibroAlmacenamiento libroAlmacenamiento;

    @Override
    public Libro guardarLibro(Libro libro) {
        return libroAlmacenamiento.guardar(libro);
    }

    @Override
    public Optional<Libro> obtenerLibroPorId(Long id) {
        return libroAlmacenamiento.obtenerPorId(id);
    }

    @Override
    public Libro obtenerLibroPorIsbn(String isbn) {
        return libroAlmacenamiento.obtenerPorIsbn(isbn)
                .orElse(null);
    }

    @Override
    public List<Libro> obtenerTodosLibros() {
        return libroAlmacenamiento.obtenerTodos();
    }

    @Override
    public void eliminarLibro(Long id) {
        libroAlmacenamiento.eliminar(id);
    }

    @Override
    public Libro actualizarLibro(Long id, Libro libroActualizado) {
        Optional<Libro> libroExistenteOptional = libroAlmacenamiento.obtenerPorId(id);

        if (libroExistenteOptional.isPresent()) {
            Libro libroExistente = libroExistenteOptional.get();
            libroActualizado.setId(libroExistente.getId()); // Aseguramos usar el ID existente
            return libroAlmacenamiento.guardar(libroActualizado);
        } else {
            return null; // O lanza una excepción
        }
    }
}