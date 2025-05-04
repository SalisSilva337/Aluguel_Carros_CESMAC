package com.example.aluguelcarros.application.repository;

import com.example.aluguelcarros.application.model.Aluguel;
import com.example.aluguelcarros.application.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AluguelRepository extends JpaRepository<Aluguel, Long> {
    List<Aluguel> findByUsuario(Usuario usuario);

    @Query("SELECT a FROM Aluguel a WHERE a.usuario.id = :usuarioId")
    List<Aluguel> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    Optional<Aluguel> findByIdAndUsuarioId(Long id, Long usuarioId);
}
