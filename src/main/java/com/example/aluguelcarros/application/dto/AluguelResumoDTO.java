package com.example.aluguelcarros.application.dto;

import java.time.LocalDate;

import com.example.aluguelcarros.application.model.Carro;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;

import com.example.aluguelcarros.application.model.Aluguel;

public class AluguelResumoDTO {
    private Long id;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Double valorTotal;
    private UsuarioDTO usuario;
    private CarroResumoDTO carro;
    private boolean devolvido;

    public AluguelResumoDTO(Aluguel aluguel) {
        this.id = aluguel.getId();
        this.dataInicio = aluguel.getDataInicio();
        this.dataFim = aluguel.getDataFim();
        this.valorTotal = aluguel.getValorTotal();
        this.usuario = new UsuarioDTO(aluguel.getUsuario());
        this.carro = new CarroResumoDTO(aluguel.getCarro());
        this.devolvido = aluguel.getDevolvido();
    }

    public boolean getDevolvido() {
        return devolvido;
    }
    public Long getId() {
        return id;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public CarroResumoDTO getCarro() {
        return carro;
    }

    public static class CarroResumoDTO {
        private Long id;
        private String modelo;
        private Integer ano;

        @Lob
        @Column(nullable = false)
        private String fotoCarro;

        public CarroResumoDTO(Carro carro) {
            this.id = carro.getId();
            this.modelo = carro.getModelo();
            this.ano = carro.getAno();
            this.fotoCarro = carro.getFotoCarro();
        }

        public Long getId() {
            return id;
        }

        public String getModelo() {
            return modelo;
        }

        public Integer getAno() {
            return ano;
        }

        public String getFotoCarro() {
            return fotoCarro;
        }
    }
}