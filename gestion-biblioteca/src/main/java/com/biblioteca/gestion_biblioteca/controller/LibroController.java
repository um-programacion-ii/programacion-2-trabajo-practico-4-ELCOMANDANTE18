package com.biblioteca.gestion_biblioteca.controller;

import com.biblioteca.gestion_biblioteca.Libro;
import com.biblioteca.gestion_biblioteca.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final LibroService libroService;

    @Autowired
    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    /**
     * Obtiene una lista de todos los libros disponibles en la biblioteca.
     *
     * @return ResponseEntity con una lista de objetos Libro (código 200 OK).
     */
    @GetMapping
    public ResponseEntity<List<Libro>> obtenerTodosLibros() {
        List<Libro> libros = libroService.obtenerTodosLibros();
        return new ResponseEntity<>(libros, HttpStatus.OK);
    }

    /**
     * Obtiene un libro específico por su ID.
     *
     * @param id El ID del libro a buscar.
     * @return ResponseEntity con el objeto Libro encontrado (código 200 OK) o
     * código 404 Not Found si no existe un libro con el ID proporcionado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Libro> obtenerLibroPorId(@PathVariable Long id) {
        Optional<Libro> libro = libroService.obtenerLibroPorId(id);
        return libro.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Crea un nuevo libro en la biblioteca.
     *
     * @param libro El objeto Libro a crear, proporcionado en el cuerpo de la solicitud (formato JSON).
     * @return ResponseEntity con el objeto Libro recién creado (código 201 Created).
     */
    @PostMapping
    public ResponseEntity<Libro> crearLibro(@RequestBody Libro libro) {
        Libro nuevoLibro = libroService.guardarLibro(libro);
        return new ResponseEntity<>(nuevoLibro, HttpStatus.CREATED);
    }

    /**
     * Actualiza la información de un libro existente.
     *
     * @param id             El ID del libro a actualizar.
     * @param libroActualizado El objeto Libro con la información actualizada,
     * proporcionado en el cuerpo de la solicitud (formato JSON).
     * @return ResponseEntity con el objeto Libro actualizado (código 200 OK) o
     * código 404 Not Found si no existe un libro con el ID proporcionado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Libro> actualizarLibro(@PathVariable Long id, @RequestBody Libro libroActualizado) {
        Libro libro = libroService.actualizarLibro(id, libroActualizado);
        if (libro != null) {
            return new ResponseEntity<>(libro, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Elimina un libro de la biblioteca por su ID.
     *
     * @param id El ID del libro a eliminar.
     * @return ResponseEntity con código 204 No Content si la eliminación fue exitosa.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLibro(@PathVariable Long id) {
        libroService.eliminarLibro(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}