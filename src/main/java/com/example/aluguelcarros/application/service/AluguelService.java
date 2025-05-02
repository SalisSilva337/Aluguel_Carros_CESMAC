package com.example.aluguelcarros.application.service;

import com.example.aluguelcarros.application.model.Aluguel;
import com.example.aluguelcarros.application.model.Carro;
import com.example.aluguelcarros.application.model.Usuario;
import com.example.aluguelcarros.application.repository.AluguelRepository;
import com.example.aluguelcarros.application.repository.CarroRepository;
import com.example.aluguelcarros.application.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class AluguelService {

    @Autowired
    private AluguelRepository aluguelRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CarroRepository carroRepository;

    public Aluguel alugarCarro(Long usuarioId, Long carroId, Date dataInicio, Date dataFim) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        Optional<Carro> carroOpt = carroRepository.findById(carroId);

        if (usuarioOpt.isPresent() && carroOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            Carro carro = carroOpt.get();

            long dias = TimeUnit.DAYS.convert(dataFim.getTime() - dataInicio.getTime(), TimeUnit.MILLISECONDS);
            double total = dias * carro.getPrecoPorDia();

            carro.setDisponivel(false);
            carroRepository.save(carro);

            Aluguel aluguel = new Aluguel(usuario, carro, dataInicio, dataFim, total, false);
            return aluguelRepository.save(aluguel);
        }

        return null;
    }

    public Aluguel devolverCarro(Long aluguelId) {
        Optional<Aluguel> aluguelOpt = aluguelRepository.findById(aluguelId);
        if (aluguelOpt.isPresent()) {
            Aluguel aluguel = aluguelOpt.get();
            aluguel.setDevolvido(true);

            Carro carro = aluguel.getCarro();
            carro.setDisponivel(true);
            carroRepository.save(carro);

            return aluguelRepository.save(aluguel);
        }

        return null;
    }

    public List<Aluguel> listarTodos() {
        return aluguelRepository.findAll();
    }

    public List<Aluguel> historicoPorUsuario(Long usuarioId) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        return usuarioOpt.map(aluguelRepository::findByUsuario).orElse(null);
    }
}