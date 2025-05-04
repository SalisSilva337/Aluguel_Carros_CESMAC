package com.example.aluguelcarros.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import com.example.aluguelcarros.application.dto.AluguelResumoDTO;
import com.example.aluguelcarros.application.model.Aluguel;
import com.example.aluguelcarros.application.model.Carro;
import com.example.aluguelcarros.application.model.Usuario;
import com.example.aluguelcarros.application.repository.AluguelRepository;
import com.example.aluguelcarros.application.repository.CarroRepository;
import com.example.aluguelcarros.application.repository.UsuarioRepository;
import com.example.aluguelcarros.application.service.AluguelService;

import jakarta.transaction.Transactional;

import com.example.aluguelcarros.application.exceptions.BusinessException;
import com.example.aluguelcarros.application.exceptions.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/alugueis")
public class AluguelController {

    @Autowired
    private AluguelService aluguelService;

    @Autowired
    private AluguelRepository aluguelRepository;

    @Autowired
    private CarroRepository carroRepository;

    private UsuarioRepository usuarioRepository;


    @GetMapping
    public List<Aluguel> listarTodos() {
        return aluguelService.listarTodos();
    }

    public AluguelController(AluguelRepository aluguelRepository, UsuarioRepository usuarioRepository) {
        this.aluguelRepository = aluguelRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/alugar")
    public ResponseEntity<?> alugarCarro(
        @RequestParam Long carroId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
        @AuthenticationPrincipal UserDetails userDetails) {
    
        try {
            // 1. Buscar usuário logado
            Usuario usuario = usuarioRepository.findByNome(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    
            // 2. Buscar carro com tratamento de erro correto
            Carro carro = carroRepository.findById(carroId)
                .orElseThrow(() -> new ResourceNotFoundException("Carro não encontrado"));
    
            // 3. Verificar disponibilidade
            if (carro.getDisponivel() == Boolean.FALSE) {
                return ResponseEntity.badRequest().body("Carro não está disponível para aluguel");
            }
    
            // 4. Validação de datas
            if (dataInicio.isBefore(LocalDate.now())) {
                return ResponseEntity.badRequest().body("Data de início não pode ser no passado");
            }
            if (dataFim.isBefore(dataInicio)) {
                return ResponseEntity.badRequest().body("Data final deve ser após a data de início");
            }
    
            // 5. Criar aluguel
            Aluguel aluguel = new Aluguel();
            aluguel.setUsuario(usuario);
            aluguel.setCarro(carro);
            aluguel.setDataInicio(dataInicio);
            aluguel.setDataFim(dataFim);
    
            // 6. Calcular valor
            long dias = ChronoUnit.DAYS.between(dataInicio, dataFim);
            dias = dias == 0 ? 1 : dias; // Mínimo 1 dia
            aluguel.setValorTotal(carro.getPrecoPorDia() * dias);
    
            // 7. Atualizar carro
            carro.setDisponivel(false);
            carroRepository.save(carro);
    
            // 8. Salvar aluguel
            Aluguel aluguelSalvo = aluguelRepository.save(aluguel);
    
            return ResponseEntity.ok(new AluguelResumoDTO(aluguelSalvo));
    
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao processar aluguel");
        }
    }
    
    @GetMapping("/meus-alugueis")
    public ResponseEntity<List<Aluguel>> getMeusAlugueis(@AuthenticationPrincipal UserDetails userDetails) {
        
        // 1. Obter o nome do usuário autenticado
        String username = userDetails.getUsername();
        
        // 2. Buscar o usuário no banco
        Usuario usuario = usuarioRepository.findByNome(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
        
        // 3. Buscar aluguéis (retorna lista vazia se não existirem)
        List<Aluguel> alugueis = aluguelRepository.findByUsuarioId(usuario.getId());
        
        return ResponseEntity.ok(alugueis);
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
    @Transactional
    public ResponseEntity<AluguelResumoDTO> devolverAluguel(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        // 1. Busca o aluguel
        Aluguel aluguel = aluguelRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Aluguel não encontrado"));

        // 2. Validação de data
        if (LocalDate.now().isBefore(aluguel.getDataInicio())) {
            throw new BusinessException("Não é possível devolver um carro antes da data de início do aluguel");
        }

        // 3. Verifica permissão
        Usuario usuario = usuarioRepository.findByNome(userDetails.getUsername())
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        
        boolean isAdmin = userDetails.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        if (!aluguel.getUsuario().getId().equals(usuario.getId()) && !isAdmin) {
            SecurityContextHolder.getContext().setAuthentication(null);
            throw new AccessDeniedException("Acesso negado: você não tem permissão para devolver este aluguel");
        }

        // 4. Lógica de devolução
        Carro carro = aluguel.getCarro();
        if (carro.getDisponivel()) {
            throw new BusinessException("Este carro já está disponível");
        }

        long dias = Math.max(1, ChronoUnit.DAYS.between(aluguel.getDataInicio(), aluguel.getDataFim()));
        aluguel.setValorTotal(carro.getPrecoPorDia() * dias);
        carro.setDisponivel(true);

        return ResponseEntity.ok(new AluguelResumoDTO(aluguel));
    }
    
    @PostMapping
    public Aluguel criarAluguel(@RequestBody Aluguel aluguel) {
        return aluguelService.salvar(aluguel);
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<?> listarPorUsuario(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        // 1. Buscar usuário logado
        Usuario usuarioLogado = usuarioRepository.findByNome(userDetails.getUsername())
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    
        // 2. Verificar se é ADMIN ou está buscando seus próprios aluguéis
        if (!usuarioLogado.getId().equals(id)) {
            boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
            
            if (!isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                       .body("Você só pode visualizar seus próprios aluguéis");
            }
        }
    
        // 3. Buscar aluguéis
        List<Aluguel> alugueis = aluguelRepository.findByUsuarioId(id);
        
        return ResponseEntity.ok(alugueis);
    }
}