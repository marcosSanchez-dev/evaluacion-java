package com.evaluacion.gateway.controller;

import com.evaluacion.gateway.dto.LoginRequest;
import com.evaluacion.gateway.model.Usuario;
import com.evaluacion.gateway.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/gateway")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsuario(request.getUsuario());

        if (usuarioOpt.isPresent() && passwordEncoder.matches(request.getPassword(), usuarioOpt.get().getPassword())) {
            return ResponseEntity.ok(Map.of("mensaje", "Login exitoso", "usuario", request.getUsuario()));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("mensaje", "Usuario o password incorrectos"));
    }
}
