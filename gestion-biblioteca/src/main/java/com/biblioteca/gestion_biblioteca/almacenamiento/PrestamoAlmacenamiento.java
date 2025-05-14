package com.biblioteca.gestion_biblioteca.almacenamiento;

import com.biblioteca.gestion_biblioteca.Prestamo;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PrestamoAlmacenamiento {

    private List<Prestamo> prestamos = new ArrayList<>();
    private long siguienteId = 1;

    public Prestamo guardar(Prestamo prestamo) {
        if (prestamo.getId() == null) {
            prestamo.setId(siguienteId++);
        } else {
            prestamos = prestamos.stream().map(p -> p.getId().equals(prestamo.getId()) ? prestamo : p).collect(Collectors.toList());
        }
        prestamos.add(prestamo);
        return prestamo;
    }

    public Optional<Prestamo> obtenerPorId(Long id) {
        return prestamos.stream().filter(prestamo -> prestamo.getId().equals(id)).findFirst();
    }

    public List<Prestamo> obtenerTodos() {
        return new ArrayList<>(prestamos);
    }

    public List<Prestamo> obtenerPrestamosPorUsuario(Long usuarioId) {
        return prestamos.stream()
                .filter(prestamo -> prestamo.getUsuario() != null && prestamo.getUsuario().getId().equals(usuarioId))
                .collect(Collectors.toList());
    }

    public List<Prestamo> obtenerPrestamosPorLibro(Long libroId) {
        return prestamos.stream()
                .filter(prestamo -> prestamo.getLibro() != null && prestamo.getLibro().getId().equals(libroId))
                .collect(Collectors.toList());
    }

    public void eliminar(Long id) {
        prestamos.removeIf(prestamo -> prestamo.getId().equals(id));
    }

    // Puedes añadir más métodos según necesites (buscar préstamos activos, etc.)
}