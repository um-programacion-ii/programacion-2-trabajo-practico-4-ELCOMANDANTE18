package com.biblioteca.gestion_biblioteca.repository;

import com.biblioteca.gestion_biblioteca.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    // Puedes añadir métodos de consulta personalizados aquí si es necesario
    Libro findByIsbn(String isbn);
}