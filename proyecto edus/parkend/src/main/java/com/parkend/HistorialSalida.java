package com.parkend;

public class HistorialSalida {
    private int id;
    private String placa;
    private String marca;
    private String modelo;
    private String tipo;
    private String horaEntrada;
    private String horaSalida;
    private long minutosTotales;
    private double totalPagado;
    private String usuario;

    public HistorialSalida() {}

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public HistorialSalida(int id, String placa, String marca, String modelo, String tipo, 
                           String horaEntrada, String horaSalida, long minutosTotales, double totalPagado) {
        this.id = id;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.tipo = tipo;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.minutosTotales = minutosTotales;
        this.totalPagado = totalPagado;
    }

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

    public String getHoraSalida() { return horaSalida; }
    public void setHoraSalida(String horaSalida) { this.horaSalida = horaSalida; }

    public long getMinutosTotales() { return minutosTotales; }
    public void setMinutosTotales(long minutosTotales) { this.minutosTotales = minutosTotales; }

    public double getTotalPagado() { return totalPagado; }
    public void setTotalPagado(double totalPagado) { this.totalPagado = totalPagado; }

    public String getTiempoFormateado() {
        long horas = minutosTotales / 60;
        long mins = minutosTotales % 60;
        if (horas > 0) {
            return horas + "h " + mins + "m";
        }
        return mins + " min";
    }
}
