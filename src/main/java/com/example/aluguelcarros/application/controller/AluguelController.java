package com.example.aluguelcarros.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.aluguelcarros.application.model.Aluguel;
import com.example.aluguelcarros.application.model.Carro;
import com.example.aluguelcarros.application.repository.AluguelRepository;
import com.example.aluguelcarros.application.repository.CarroRepository;
import com.example.aluguelcarros.application.service.AluguelService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/alugueis")
public class AluguelController {

    @Autowired
    private AluguelService aluguelService;

    @Autowired
    private AluguelRepository aluguelRepository;

    @Autowired
    private CarroRepository carroRepository;


    @GetMapping
    public List<Aluguel> listarTodos() {
        return aluguelService.listarTodos();
    }

    @PostMapping("/alugar")
    public Aluguel alugarCarro(
        @RequestParam Long carroId,
        @RequestParam Long usuarioId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        return aluguelService.alugarCarro(carroId, usuarioId, dataInicio, dataFim);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        aluguelService.deletar(id);
    }

    @GetMapping("/disponiveis")
    public List<Carro> listarCarrosDisponiveis() {
        return aluguelService.listarCarrosDisponiveis();
    }

    @PutMapping("/devolver/{id}")
    public ResponseEntity<?> devolverAluguel(@PathVariable Long id) {
        Optional<Aluguel> aluguelOptional = aluguelRepository.findById(id);

        if (aluguelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aluguel não encontrado.");
        }

        Aluguel aluguel = aluguelOptional.get();
        Carro carro = aluguel.getCarro();

        long dias = ChronoUnit.DAYS.between(aluguel.getDataInicio(), aluguel.getDataFim());
        if (dias == 0)
            dias = 1; // garante ao menos 1 dia

        double total = carro.getPrecoPorDia() * dias;
        aluguel.setValorTotal(total);

        carro.setDisponivel(true);
        carroRepository.save(carro);

        aluguelRepository.save(aluguel);

        return ResponseEntity.ok(aluguel);
    }
    
    @PostMapping
    public Aluguel criarAluguel(@RequestBody Aluguel aluguel) {
        return aluguelService.salvar(aluguel);
    }

    @GetMapping("/usuario/{id}")
    public List<Aluguel> listarPorUsuario(@PathVariable Long id) {
        return aluguelRepository.findByUsuarioId(id);
    }

}