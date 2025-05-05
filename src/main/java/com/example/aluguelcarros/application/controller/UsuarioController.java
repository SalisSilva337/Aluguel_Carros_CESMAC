package com.example.aluguelcarros.application.controller;

import com.example.aluguelcarros.application.config.JwtTokenService;
import com.example.aluguelcarros.application.dto.UsuarioDTO;
import com.example.aluguelcarros.application.model.Usuario;
import com.example.aluguelcarros.application.model.Usuario.Role;
import com.example.aluguelcarros.application.repository.UsuarioRepository;
import com.example.aluguelcarros.application.service.UsuarioService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    private UsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenService jwtTokenService;

    public UsuarioController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Cadastro com senha criptografada
    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> criarUsuario(@RequestBody @Valid Usuario usuario, BindingResult result) {
        // Validação simplificada mas eficaz
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(
                result.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage
                    ))
            );
        }

        if (usuarioRepository.existsByNome(usuario.getNome())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("erro", "Nome de usuário já existe"));
        }

        // Garante role USER e senha hasheada (criptografada)
        usuario.setRole(Role.USER);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(
            Map.of(
                "id", usuarioSalvo.getId(),
                "nome", usuarioSalvo.getNome(),
                "role", usuarioSalvo.getRole().name(),
                "mensagem", "Usuário criado com sucesso"
            )
        );
    }

    // Login com verificação segura (o pai é bom demais)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {
        try {
            Usuario user = usuarioService.login(usuario.getNome(), usuario.getSenha());
            
            // Gera o token JWT
            String token = jwtTokenService.generateToken(
                user.getNome(), 
                user.getRole().name(),
                user.getId()
            );
            
            // Retorna o token + DTO do usuário
            return ResponseEntity.ok(Map.of(
                "token", token,
                "usuario", new UsuarioDTO(user)
            ));
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    @GetMapping("/{id}")
    public Usuario buscarUsuario(@PathVariable Long id) {
        return usuarioService.buscarPorId(id);
    }
}