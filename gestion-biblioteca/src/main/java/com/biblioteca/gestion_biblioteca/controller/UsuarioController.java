package com.biblioteca.gestion_biblioteca.controller;

import com.biblioteca.gestion_biblioteca.Usuario;
import com.biblioteca.gestion_biblioteca.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Obtiene una lista de todos los usuarios registrados en la biblioteca.
     *
     * @return ResponseEntity con una lista de objetos Usuario (código 200 OK).
     */
    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodosUsuarios() {
        List<Usuario> usuarios = usuarioService.obtenerTodosUsuarios();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    /**
     * Obtiene un usuario específico por su ID.
     *
     * @param id El ID del usuario a buscar.
     * @return ResponseEntity con el objeto Usuario encontrado (código 200 OK) o
     * código 404 Not Found si no existe un usuario con el ID proporcionado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(id);
        return usuario.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Crea un nuevo usuario en la biblioteca.
     *
     * @param usuario El objeto Usuario a crear, proporcionado en el cuerpo de la solicitud (formato JSON).
     * @return ResponseEntity con el objeto Usuario recién creado (código 201 Created).
     */
    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioService.guardarUsuario(usuario);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    /**
     * Actualiza la información de un usuario existente.
     *
     * @param id             El ID del usuario a actualizar.
     * @param usuarioActualizado El objeto Usuario con la información actualizada,
     * proporcionado en el cuerpo de la solicitud (formato JSON).
     * @return ResponseEntity con el objeto Usuario actualizado (código 200 OK) o
     * código 404 Not Found si no existe un usuario con el ID proporcionado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioActualizado) {
        Usuario usuario = usuarioService.actualizarUsuario(id, usuarioActualizado);
        if (usuario != null) {
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Elimina un usuario de la biblioteca por su ID.
     *
     * @param id El ID del usuario a eliminar.
     * @return ResponseEntity con código 204 No Content si la eliminación fue exitosa.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}