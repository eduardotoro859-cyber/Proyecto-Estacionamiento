package com.parkend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ParkendApplicationTests {

    @Autowired
    private ConexionRepository conexionRepository;

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private UsuarioService usuarioService;

    @Test
    void contextLoads() {
        assertNotNull(conexionRepository, "ConexionRepository debe estar inyectado");
        assertNotNull(vehiculoService, "VehiculoService debe estar inyectado");
        assertNotNull(usuarioService, "UsuarioService debe estar inyectado");
    }
}
