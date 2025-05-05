package com.example.aluguelcarros.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.aluguelcarros.application.model.Carro;
import com.example.aluguelcarros.application.model.Aluguel;
import com.example.aluguelcarros.application.model.Usuario;
import com.example.aluguelcarros.application.repository.AluguelRepository;
import com.example.aluguelcarros.application.repository.CarroRepository;
import com.example.aluguelcarros.application.repository.UsuarioRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AluguelService {

    @Autowired
    private final AluguelRepository aluguelRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CarroRepository carroRepository;

    public List<Aluguel> listarTodos() {
        return aluguelRepository.findAll();
    }

    public AluguelService(AluguelRepository aluguelRepository) {
        this.aluguelRepository = aluguelRepository;
    }

    public Aluguel alugarCarro(Long carroId, Long usuarioId, LocalDate dataInicio, LocalDate dataFim) {
        Carro carro = carroRepository.findById(carroId)
                .orElseThrow(() -> new RuntimeException("Carro não encontrado"));
    
        if (!carro.getDisponivel()) {
            throw new RuntimeException("Carro indisponível");
        }
    
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    
        // Calcula o número de dias entre a data de início e fim
        long dias = ChronoUnit.DAYS.between(dataInicio, dataFim);
        if (dias <= 0) {
            throw new RuntimeException("A data final deve ser após a data de início");
        }
    
        // Calcula o valor total
        double valorTotal = carro.getPrecoPorDia() * dias;
    
        // Cria o aluguel e configura os dados
        Aluguel aluguel = new Aluguel();
        aluguel.setCarro(carro);
        aluguel.setUsuario(usuario);
        aluguel.setDataInicio(dataInicio);
        aluguel.setDataFim(dataFim);
        aluguel.setValorTotal(valorTotal); // Setando o valor total calculado
    
        // Marca o carro como disponivel = false
        carro.setDisponivel(false);
        carroRepository.save(carro);
    
        // Salva o aluguel no banco de dados
        return aluguelRepository.save(aluguel);
    }

    public void deletar(Long id) {
        Aluguel aluguel = aluguelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluguel não encontrado"));

        Carro carro = aluguel.getCarro();
        carro.setDisponivel(true);
        carroRepository.save(carro);

        aluguelRepository.deleteById(id);
    }

    public List<Aluguel> findByUsuarioId(Long usuarioId) {
        return aluguelRepository.findByUsuarioId(usuarioId);
    }

    public List<Carro> listarCarrosDisponiveis() {
        return carroRepository.findByDisponivelTrue();
    }

    public Aluguel salvar(Aluguel aluguel) {
        long dias = ChronoUnit.DAYS.between(aluguel.getDataInicio(), aluguel.getDataFim());
        double total = dias * aluguel.getCarro().getPrecoPorDia();
        aluguel.setValorTotal(total);

        aluguel.getCarro().setDisponivel(false); // Marca o carro como disponvel = false também
        return aluguelRepository.save(aluguel);
    }
}