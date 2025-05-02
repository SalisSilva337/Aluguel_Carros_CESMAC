package com.example.aluguelcarros.application.repository;

import com.example.aluguelcarros.application.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}