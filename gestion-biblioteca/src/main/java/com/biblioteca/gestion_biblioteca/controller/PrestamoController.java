package com.biblioteca.gestion_biblioteca.controller;

import com.biblioteca.gestion_biblioteca.Libro;
import com.biblioteca.gestion_biblioteca.Prestamo;
import com.biblioteca.gestion_biblioteca.Usuario;
import com.biblioteca.gestion_biblioteca.service.LibroService;
import com.biblioteca.gestion_biblioteca.service.PrestamoService;
import com.biblioteca.gestion_biblioteca.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;
    private final LibroService libroService;
    private final UsuarioService usuarioService;

    @Autowired
    public PrestamoController(PrestamoService prestamoService, LibroService libroService, UsuarioService usuarioService) {
        this.prestamoService = prestamoService;
        this.libroService = libroService;
        this.usuarioService = usuarioService;
    }

    /**
     * Obtiene una lista de todos los préstamos activos e históricos.
     *
     * @return ResponseEntity con una lista de objetos Prestamo (código 200 OK).
     */
    @GetMapping
    public ResponseEntity<List<Prestamo>> obtenerTodosPrestamos() {
        List<Prestamo> prestamos = prestamoService.obtenerTodosPrestamos();
        return new ResponseEntity<>(prestamos, HttpStatus.OK);
    }

    /**
     * Obtiene un préstamo específico por su ID.
     *
     * @param id El ID del préstamo a buscar.
     * @return ResponseEntity con el objeto Prestamo encontrado (código 200 OK) o
     * código 404 Not Found si no existe un préstamo con el ID proporcionado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Prestamo> obtenerPrestamoPorId(@PathVariable Long id) {
        Optional<Prestamo> prestamo = prestamoService.obtenerPrestamoPorId(id);
        return prestamo.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Realiza un nuevo préstamo de un libro a un usuario.
     *
     * @param libroId         El ID del libro a prestar (pasado como parámetro de consulta).
     * @param usuarioId       El ID del usuario que realiza el préstamo (pasado como parámetro de consulta).
     * @param fechaPrestamo   La fecha en que se realiza el préstamo (pasada como parámetro de consulta, formato ISO 8601).
     * @param fechaDevolucion La fecha esperada de devolución (pasada como parámetro de consulta, formato ISO 8601).
     * @return ResponseEntity con el objeto Prestamo recién creado (código 201 Created) o
     * código 404 Not Found si el libro o el usuario no existen.
     */
    @PostMapping
    public ResponseEntity<Prestamo> realizarPrestamo(@RequestParam Long libroId,
                                                     @RequestParam Long usuarioId,
                                                     @RequestParam LocalDate fechaPrestamo,
                                                     @RequestParam LocalDate fechaDevolucion) {
        Optional<Libro> libro = libroService.obtenerLibroPorId(libroId);
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(usuarioId);

        if (libro.isPresent() && usuario.isPresent()) {
            Prestamo nuevoPrestamo = prestamoService.realizarPrestamo(libro.get(), usuario.get(), fechaPrestamo, fechaDevolucion);
            return new ResponseEntity<>(nuevoPrestamo, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Marca un préstamo como devuelto, registrando la fecha de devolución real.
     *
     * @param id              El ID del préstamo a marcar como devuelto (pasado como parámetro de ruta).
     * @param fechaDevolucion La fecha real de devolución (pasada como parámetro de consulta, formato ISO 8601).
     * @return ResponseEntity con código 200 OK si la operación fue exitosa.
     */
    @PutMapping("/{id}/devolver")
    public ResponseEntity<Void> marcarComoDevuelto(@PathVariable Long id, @RequestParam LocalDate fechaDevolucion) {
        prestamoService.marcarComoDevuelto(id, fechaDevolucion);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Elimina un préstamo por su ID.
     *
     * @param id El ID del préstamo a eliminar.
     * @return ResponseEntity con código 204 No Content si la eliminación fue exitosa.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPrestamo(@PathVariable Long id) {
        prestamoService.eliminarPrestamo(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}