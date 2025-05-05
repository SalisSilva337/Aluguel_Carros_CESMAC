package com.example.aluguelcarros.application.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class Carro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String modelo;
    private int ano;
    private double precoPorDia;
    private Boolean disponivel = true;

    @Lob
    @Column(nullable = false)
    private String fotoCarro;

    public Carro() {}

    public Carro(String modelo, int ano, double precoPorDia, boolean disponivel) {
        this.modelo = modelo;
        this.ano = ano;
        this.precoPorDia = precoPorDia;
        this.disponivel = disponivel;
    }

    public Long getId() {
        return id;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public double getPrecoPorDia() {
        return precoPorDia;
    }

    public void setPrecoPorDia(double precoPorDia) {
        this.precoPorDia = precoPorDia;
    }

    public Boolean getDisponivel() {
        return disponivel != null ? disponivel : true;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public String getFotoCarro() {
        return fotoCarro;
    }

    public void setFotoCarro(String fotoCarro) {
        this.fotoCarro = fotoCarro;
    }
}