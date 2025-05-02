package com.example.aluguelcarros.application.controller;

import com.example.aluguelcarros.application.model.Aluguel;
import com.example.aluguelcarros.application.service.AluguelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/alugueis")
public class AluguelController {

    @Autowired
    private AluguelService aluguelService;

    @PostMapping("/alugar")
    public Aluguel alugarCarro(
            @RequestParam Long usuarioId,
            @RequestParam Long carroId,
            @RequestParam Date dataInicio,
            @RequestParam Date dataFim
    ) {
        return aluguelService.alugarCarro(usuarioId, carroId, dataInicio, dataFim);
    }

    @PutMapping("/devolver/{id}")
    public Aluguel devolverCarro(@PathVariable Long id) {
        return aluguelService.devolverCarro(id);
    }

    @GetMapping
    public List<Aluguel> listarTodos() {
        return aluguelService.listarTodos();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Aluguel> historicoUsuario(@PathVariable Long usuarioId) {
        return aluguelService.historicoPorUsuario(usuarioId);
    }
}
