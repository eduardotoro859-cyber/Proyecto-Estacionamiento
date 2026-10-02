package com.parkend;

public class Camion extends Vehiculo {

    public static final double TARIFA_HORA = 4.0;

    public Camion() {
        super("CAMION");
    }

    @Override
    public double getTarifaPorHora() {
        return TARIFA_HORA;
    }

    @Override
    public String getDescripcionTipo() {
        return "Camión / Vehículo Pesado";
    }
}
