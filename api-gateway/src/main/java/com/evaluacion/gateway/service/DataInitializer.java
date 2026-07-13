package com.evaluacion.gateway.service;

import com.evaluacion.gateway.model.Usuario;
import com.evaluacion.gateway.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public DataInitializer(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) {
        if (usuarioRepository.findByUsuario("admin").isEmpty()) {
            Usuario admin = new Usuario("admin", passwordEncoder.encode("admin123"));
            usuarioRepository.save(admin);
            System.out.println(">> Usuario demo creado -> usuario: admin / password: admin123");
        }
    }
}
