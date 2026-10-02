package com.parkend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        // La inicialización de la BD ahora se delega al bean ConexionRepository
        // que Spring invoca mediante @PostConstruct al arrancar
        SpringApplication.run(Main.class, args);
        System.out.println("✅ Aplicacion Parkend iniciada con éxito en http://localhost:8080");
    }
}
