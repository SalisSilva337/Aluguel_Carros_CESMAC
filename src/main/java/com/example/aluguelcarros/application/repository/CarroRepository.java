package com.example.aluguelcarros.application.repository;

import com.example.aluguelcarros.application.model.Carro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CarroRepository extends JpaRepository<Carro, Long> {
    List<Carro> findByDisponivelTrue();
}