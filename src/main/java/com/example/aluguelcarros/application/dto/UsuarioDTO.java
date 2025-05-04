package com.example.aluguelcarros.application.dto;

import com.example.aluguelcarros.application.model.Usuario;

public record UsuarioDTO(Long id, String nome) {
    public UsuarioDTO(Usuario usuario) {
        this(usuario.getId(), usuario.getNome());
    }
}