package com.parkend;

public class VehiculoFactory {

    /**
     * Instancia polimórficamente la subclase correcta de Vehiculo
     * según el tipo especificado ("CARRO", "MOTO", "CAMION").
     */
    public static Vehiculo crearVehiculo(String tipo) {
        if (tipo == null) {
            return new Carro();
        }
        switch (tipo.trim().toUpperCase()) {
            case "MOTO":
                return new Moto();
            case "CAMION":
                return new Camion();
            case "CARRO":
            default:
                return new Carro();
        }
    }

    /**
     * Construye un vehículo completo a partir de los datos de la base de datos.
     */
    public static Vehiculo crearVehiculo(int id, String placa, String marca, String modelo, String tipo, String horaEntrada) {
        Vehiculo v = crearVehiculo(tipo);
        v.setId(id);
        v.setPlaca(placa != null ? placa.toUpperCase() : "");
        v.setMarca(marca != null ? marca.toUpperCase() : "");
        v.setModelo(modelo != null ? modelo.toUpperCase() : "");
        v.setHoraEntrada(horaEntrada);
        return v;
    }

    public static Vehiculo crearVehiculo(int id, String placa, String marca, String modelo, String tipo, String horaEntrada, String usuario) {
        Vehiculo v = crearVehiculo(id, placa, marca, modelo, tipo, horaEntrada);
        v.setUsuario(usuario);
        return v;
    }
}
