package com.example.aluguelcarros.application.service;

import com.example.aluguelcarros.application.model.Usuario;
import com.example.aluguelcarros.application.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario criarUsuario(Usuario usuario) {
        //System.out.println("Senha recebida: " + usuario.getSenha()); // Debug

        if (usuarioRepository.existsByNome(usuario.getNome())) {
            throw new DataIntegrityViolationException("Usuário já cadastrado");
        }
        
        if (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty()) {
            throw new IllegalArgumentException("Senha é obrigatória");
        }
    
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        return usuarioRepository.save(usuario);
    }


    public Usuario login(String nome, String senha) {
        Usuario usuario = usuarioRepository.findByNome(nome)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Verificar se a senha informada bate com a senha criptografada
        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new RuntimeException("Senha incorreta");
        }

        return usuario;
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }
}