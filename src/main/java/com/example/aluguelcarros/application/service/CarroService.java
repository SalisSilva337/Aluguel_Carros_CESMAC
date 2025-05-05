package com.example.aluguelcarros.application.service;

import com.example.aluguelcarros.application.model.Carro;
import com.example.aluguelcarros.application.repository.CarroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarroService {

    @Autowired
    private CarroRepository carroRepository;

    public Carro cadastrarCarro(Carro carro) {
        return carroRepository.save(carro);
    }

    public List<Carro> listarTodos() {
        return carroRepository.findAll();
    }

    public List<Carro> listarDisponiveis() {
        return carroRepository.findByDisponivelTrue();
    }

    public Carro buscarPorId(Long id) {
        return carroRepository.findById(id).orElse(null);
    }

    public Carro atualizarCarro(Carro carro) {
        return carroRepository.save(carro);
    }
}