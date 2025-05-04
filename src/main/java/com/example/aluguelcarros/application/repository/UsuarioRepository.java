package com.example.aluguelcarros.application.repository;

import com.example.aluguelcarros.application.model.Usuario;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByNome(String nome);
    boolean existsByNome(String nome);
}