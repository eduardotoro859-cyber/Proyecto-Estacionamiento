package com.parkend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class VehiculoService {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<Vehiculo> listarVehiculos(String usuario, String placaBusqueda) {
        return vehiculoRepository.obtenerVehiculos(usuario, placaBusqueda);
    }

    public void registrarIngreso(VehiculoForm form, String usuario) {
        Vehiculo vehiculo = VehiculoFactory.crearVehiculo(form.getTipo());
        String placaLimpia = form.getPlaca() != null ? form.getPlaca().trim().toUpperCase().replaceAll("[^A-Z0-9]", "") : "";
        if (placaLimpia.length() > 7) {
            placaLimpia = placaLimpia.substring(0, 7);
        }
        vehiculo.setPlaca(placaLimpia);
        vehiculo.setMarca(form.getMarca() != null ? form.getMarca().trim().toUpperCase() : "");
        vehiculo.setModelo(form.getModelo() != null ? form.getModelo().trim().toUpperCase() : "");
        vehiculo.setUsuario(usuario);
        vehiculoRepository.registrarVehiculo(vehiculo, usuario);
    }

    public TicketCobro generarTicket(int vehiculoId, String usuario) {
        Vehiculo v = vehiculoRepository.obtenerVehiculoPorId(vehiculoId, usuario);
        if (v == null) return null;

        LocalDateTime ahora = LocalDateTime.now();
        long minutos = 1;

        try {
            LocalDateTime entrada = LocalDateTime.parse(v.getHoraEntrada(), FMT);
            minutos = java.time.Duration.between(entrada, ahora).toMinutes();
            if (minutos <= 0) minutos = 1;
        } catch (Exception e) {
            System.err.println("Aviso al parsear hora de entrada: " + e.getMessage());
        }

        TicketCobro ticket = new TicketCobro();
        ticket.setVehiculoId(v.getId());
        ticket.setPlaca(v.getPlaca());
        ticket.setMarca(v.getMarca());
        ticket.setModelo(v.getModelo());
        ticket.setTipo(v.getTipo());
        ticket.setHoraEntrada(v.getHoraEntrada());
        ticket.setHoraSalida(ahora.format(FMT));
        ticket.setMinutosTotales(minutos);
        ticket.setUsuario(usuario);

        // Polimorfismo: cada tipo de vehículo calcula su tarifa y cobro
        ticket.setTarifaPorHora(v.getTarifaPorHora());
        ticket.setTotalPagar(v.calcularCobro(minutos));

        return ticket;
    }

    public boolean procesarSalida(TicketCobro ticket, String usuario) {
        return vehiculoRepository.procesarSalida(ticket, usuario);
    }

    public void cancelarIngreso(int vehiculoId, String usuario) {
        vehiculoRepository.eliminarVehiculo(vehiculoId, usuario);
    }

    public List<HistorialSalida> obtenerHistorial(String usuario) {
        return vehiculoRepository.obtenerHistorial(usuario);
    }

    public double obtenerTotalRecaudado(String usuario) {
        return vehiculoRepository.obtenerTotalRecaudado(usuario);
    }
}
