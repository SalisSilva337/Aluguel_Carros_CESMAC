package com.example.aluguelcarros.application.repository;

import com.example.aluguelcarros.application.model.Aluguel;
import com.example.aluguelcarros.application.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AluguelRepository extends JpaRepository<Aluguel, Long> {
    List<Aluguel> findByUsuario(Usuario usuario);
}