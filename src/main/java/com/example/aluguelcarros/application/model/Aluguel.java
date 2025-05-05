package com.example.aluguelcarros.application.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Aluguel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dataInicio;
    private LocalDate dataFim;

    private Double valorTotal;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "carro_id")
    private Carro carro;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) {this.dataFim = dataFim; }
    
    public Double getValorTotal() { return valorTotal; }
    public void setValorTotal(Double valorTotal) { this.valorTotal = valorTotal; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Carro getCarro() { return carro; }
    public void setCarro(Carro carro) { this.carro = carro; }
}