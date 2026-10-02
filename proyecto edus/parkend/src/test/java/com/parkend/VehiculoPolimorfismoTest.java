package com.parkend;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VehiculoPolimorfismoTest {

    @Test
    public void testFactoryInstanciaSubclasesCorrectas() {
        Vehiculo moto = VehiculoFactory.crearVehiculo("MOTO");
        Vehiculo carro = VehiculoFactory.crearVehiculo("CARRO");
        Vehiculo camion = VehiculoFactory.crearVehiculo("CAMION");

        assertTrue(moto instanceof Moto, "El tipo MOTO debe instanciar la clase Moto");
        assertTrue(carro instanceof Carro, "El tipo CARRO debe instanciar la clase Carro");
        assertTrue(camion instanceof Camion, "El tipo CAMION debe instanciar la clase Camion");
    }

    @Test
    public void testTarifasPolimorficasPorHora() {
        Vehiculo moto = new Moto();
        Vehiculo carro = new Carro();
        Vehiculo camion = new Camion();

        assertEquals(1.0, moto.getTarifaPorHora());
        assertEquals(2.0, carro.getTarifaPorHora());
        assertEquals(4.0, camion.getTarifaPorHora());
    }

    @Test
    public void testCalculoCobroSegunTiempo() {
        Vehiculo moto = new Moto();
        Vehiculo carro = new Carro();
        Vehiculo camion = new Camion();

        // 30 minutos -> cobra 1 hora mínima
        assertEquals(1.0, moto.calcularCobro(30));
        assertEquals(2.0, carro.calcularCobro(30));
        assertEquals(4.0, camion.calcularCobro(30));

        // 75 minutos (1h 15m) -> cobra 2 horas
        assertEquals(2.0, moto.calcularCobro(75));
        assertEquals(4.0, carro.calcularCobro(75));
        assertEquals(8.0, camion.calcularCobro(75));
    }
}
