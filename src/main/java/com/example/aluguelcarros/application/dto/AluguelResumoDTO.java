package com.example.aluguelcarros.application.dto;

import java.time.LocalDate;

import com.example.aluguelcarros.application.model.Carro;
import com.example.aluguelcarros.application.model.Aluguel;

public class AluguelResumoDTO {
    private Long id;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Double valorTotal;
    private CarroResumoDTO carro;

    public AluguelResumoDTO(Aluguel aluguel) {
        this.id = aluguel.getId();
        this.dataInicio = aluguel.getDataInicio();
        this.dataFim = aluguel.getDataFim();
        this.valorTotal = aluguel.getValorTotal();
        this.carro = new CarroResumoDTO(aluguel.getCarro());
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

    public CarroResumoDTO getCarro() {
        return carro;
    }

    public static class CarroResumoDTO {
        private Long id;
        private String modelo;
        private Integer ano;

        public CarroResumoDTO(Carro carro) {
            this.id = carro.getId();
            this.modelo = carro.getModelo();
            this.ano = carro.getAno();
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
    }
}