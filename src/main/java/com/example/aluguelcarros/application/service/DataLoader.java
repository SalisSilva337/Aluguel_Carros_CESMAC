package com.example.aluguelcarros.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.aluguelcarros.application.model.Carro;
import com.example.aluguelcarros.application.repository.CarroRepository;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CarroRepository carroRepository;

    @Override
    public void run(String... args) throws Exception {
        if (carroRepository.count() == 0) {
            carroRepository.save(new Carro("Chevrolet Impala", 1967, 750.0, true, "Impala67.jpeg"));
            carroRepository.save(new Carro("Rolls Royce Phantom", 2019, 2000.0, true, "RollsRoycePanthom.jpeg"));
            carroRepository.save(new Carro("Toyota Supra MK4", 1998, 800.0, true, "ToyotaSupraMK4.jpeg"));
            carroRepository.save(new Carro("Mitsubishi Eclipse", 1999, 500.0, true, "MitsubishiEclipse.jpeg"));
        }
    }
}
