package com.biblioteca.gestion_biblioteca.repository;

import com.biblioteca.gestion_biblioteca.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Puedes añadir métodos de consulta personalizados aquí si es necesario
    Usuario findByEmail(String email);
    }