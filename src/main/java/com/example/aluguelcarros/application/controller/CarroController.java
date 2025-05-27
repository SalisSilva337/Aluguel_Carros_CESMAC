package com.example.aluguelcarros.application.controller;

import com.example.aluguelcarros.application.model.Carro;
import com.example.aluguelcarros.application.service.CarroService;
import com.example.aluguelcarros.application.service.FileStorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/carros")
public class CarroController {

    @Autowired
    private CarroService carroService;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping
    public ResponseEntity<Carro> cadastrarCarro(
            @RequestParam("modelo") String modelo,
            @RequestParam("ano") int ano,
            @RequestParam("precoPorDia") double precoPorDia,
            @RequestParam(value = "disponivel", defaultValue = "true") boolean disponivel,
            @RequestParam("fotoCarro") MultipartFile fotoCarro) throws IOException { // Troquei para fotoCarro

        if (fotoCarro.isEmpty()) {
            throw new IllegalArgumentException("A foto do carro é obrigatória!");
        }

        String nomeArquivo = fileStorageService.saveFile(fotoCarro); // Salva a imagem

        Carro carro = new Carro(modelo, ano, precoPorDia, disponivel, nomeArquivo);
        carro.setFotoCarro(nomeArquivo); // Usa o novo nome do campo

        return ResponseEntity.ok(carroService.cadastrarCarro(carro));
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