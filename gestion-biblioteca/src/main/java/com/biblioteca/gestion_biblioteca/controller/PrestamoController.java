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

    @GetMapping
    public ResponseEntity<List<Prestamo>> obtenerTodosPrestamos() {
        List<Prestamo> prestamos = prestamoService.obtenerTodosPrestamos();
        return new ResponseEntity<>(prestamos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prestamo> obtenerPrestamoPorId(@PathVariable Long id) {
        Optional<Prestamo> prestamo = prestamoService.obtenerPrestamoPorId(id);
        return prestamo.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

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

    @PutMapping("/{id}/devolver")
    public ResponseEntity<Void> marcarComoDevuelto(@PathVariable Long id, @RequestParam LocalDate fechaDevolucion) {
        prestamoService.marcarComoDevuelto(id, fechaDevolucion);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPrestamo(@PathVariable Long id) {
        prestamoService.eliminarPrestamo(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}