package com.parkend;

public class Carro extends Vehiculo {

    public static final double TARIFA_HORA = 2.0;

    public Carro() {
        super("CARRO");
    }

    @Override
    public double getTarifaPorHora() {
        return TARIFA_HORA;
    }

    @Override
    public String getDescripcionTipo() {
        return "Automóvil / Camioneta";
    }
}
