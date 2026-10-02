package com.parkend;

public class TicketCobro {
    private int vehiculoId;
    private String placa;
    private String marca;
    private String modelo;
    private String tipo;
    private String horaEntrada;
    private String horaSalida;
    private long minutosTotales;
    private double tarifaPorHora;
    private double totalPagar;
    private String usuario;

    public TicketCobro() {}

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public int getVehiculoId() { return vehiculoId; }
    public void setVehiculoId(int vehiculoId) { this.vehiculoId = vehiculoId; }

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

    public double getTarifaPorHora() { return tarifaPorHora; }
    public void setTarifaPorHora(double tarifaPorHora) { this.tarifaPorHora = tarifaPorHora; }

    public double getTotalPagar() { return totalPagar; }
    public void setTotalPagar(double totalPagar) { this.totalPagar = totalPagar; }

    public String getTiempoFormateado() {
        long horas = minutosTotales / 60;
        long mins = minutosTotales % 60;
        if (horas > 0) {
            return horas + " hora(s) y " + mins + " minuto(s)";
        }
        return mins + " minuto(s)";
    }
}
