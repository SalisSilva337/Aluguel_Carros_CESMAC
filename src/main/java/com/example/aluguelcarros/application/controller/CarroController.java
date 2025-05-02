package com.example.aluguelcarros.application.controller;

import com.example.aluguelcarros.application.model.Carro;
import com.example.aluguelcarros.application.service.CarroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carros")
public class CarroController {

    @Autowired
    private CarroService carroService;

    @PostMapping
    public Carro cadastrarCarro(@RequestBody Carro carro) {
        return carroService.cadastrarCarro(carro);
    }

    @GetMapping
    public List<Carro> listarTodos() {
        return carroService.listarTodos();
    }

    @GetMapping("/disponiveis")
    public List<Carro> listarDisponiveis() {
        return carroService.listarDisponiveis();
    }

    @GetMapping("/{id}")
    public Carro buscarPorId(@PathVariable Long id) {
        return carroService.buscarPorId(id);
    }
}