package com.biblioteca.gestion_biblioteca;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    // Podrías añadir más campos relevantes para tu sistema de usuarios
}