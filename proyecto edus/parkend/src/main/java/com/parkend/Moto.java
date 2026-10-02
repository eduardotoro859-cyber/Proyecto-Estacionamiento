package com.parkend;

public class Moto extends Vehiculo {

    public static final double TARIFA_HORA = 1.0;

    public Moto() {
        super("MOTO");
    }

    @Override
    public double getTarifaPorHora() {
        return TARIFA_HORA;
    }

    @Override
    public String getDescripcionTipo() {
        return "Motocicleta";
    }
}
