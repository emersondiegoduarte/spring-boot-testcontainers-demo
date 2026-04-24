package com.example.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private UsuarioController controller;

    @MockitoBean
    private UsuarioRepository repository;

    @Test
    void shouldSaveUsuarioThroughEndpoint() {
        UsuarioCreateRequest request = new UsuarioCreateRequest("Diego");
        Usuario saved = new Usuario();
        saved.setNome("Diego");
        when(repository.save(any(Usuario.class))).thenReturn(saved);

        ResponseEntity<Usuario> response = controller.create(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Diego", response.getBody().getNome());
        verify(repository).save(any(Usuario.class));
    }
}

