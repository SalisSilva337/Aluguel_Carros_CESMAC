package com.example.aluguelcarros.application;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.aluguelcarros.application.model.Usuario;
import com.example.aluguelcarros.application.repository.UsuarioRepository;

@SpringBootApplication
public class AluguelCarrosApplication {
	public static void main(String[] args) {
		SpringApplication.run(AluguelCarrosApplication.class, args);
	}

    @Bean
    public CommandLineRunner criarAdmin(UsuarioRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (!repo.existsByNome("admin")) {
                Usuario admin = new Usuario();
                admin.setNome("admin");
                admin.setSenha(encoder.encode("admin123")); 
                admin.setRole(Usuario.Role.ADMIN); // Use o enum corretamente
                repo.save(admin);
                System.out.println("✅ Admin criado!");
            }
        };
    }
}