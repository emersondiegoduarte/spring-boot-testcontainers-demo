package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository repository;

    public UsuarioController(UsuarioRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Usuario> create(@RequestBody UsuarioCreateRequest request) {
        Usuario usuario = new Usuario();
        usuario.setNome(request.nome());

        Usuario saved = repository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}

