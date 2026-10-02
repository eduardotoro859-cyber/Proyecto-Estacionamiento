package com.parkend;

public abstract class Vehiculo {
    private int id;
    private String placa;
    private String marca;
    private String modelo;
    private String tipo;
    private String horaEntrada;
    private String usuario;

    public Vehiculo() {}

    public Vehiculo(String tipo) {
        this.tipo = tipo;
    }

    public Vehiculo(String tipo, String usuario) {
        this.tipo = tipo;
        this.usuario = usuario;
    }

    // Métodos polimórficos
    public abstract double getTarifaPorHora();
    public abstract String getDescripcionTipo();

    /**
     * Calcula el monto a pagar según los minutos de permanencia.
     * Aplica la regla estándar de parqueadero: mínimo 1 hora, redondeando
     * hacia arriba las horas o fracciones de hora transcurridas.
     */
    public double calcularCobro(long minutos) {
        long horasACobrar = (long) Math.ceil(minutos / 60.0);
        if (horasACobrar <= 0) {
            horasACobrar = 1;
        }
        return horasACobrar * getTarifaPorHora();
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getHoraEntrada() { return horaEntrada; }
    public void setHoraEntrada(String horaEntrada) { this.horaEntrada = horaEntrada; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
}
