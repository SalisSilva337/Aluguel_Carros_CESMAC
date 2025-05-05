package com.example.aluguelcarros.application.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.aluguelcarros.application.model.Usuario;
import com.example.aluguelcarros.application.repository.UsuarioRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    // Injeção por construtor (mais seguro que @Autowired)
    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByNome(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário '" + username + "' não encontrado"));
        
        return new org.springframework.security.core.userdetails.User(
            usuario.getNome(),
            usuario.getSenha(),
            usuario.getAuthorities() // criar esse metodo em Usuario pra nao esquecer, pq fazer isso 5 da manha é de quebrar o caba
        );
    }
}