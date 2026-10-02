package com.parkend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class VehiculoRepository {

    @Autowired
    private ConexionRepository conexion;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<Vehiculo> obtenerVehiculos(String usuario, String placaBusqueda) {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT * FROM vehiculos WHERE usuario = ?";
        if (placaBusqueda != null && !placaBusqueda.trim().isEmpty()) {
            sql += " AND placa LIKE ?";
        }
        sql += " ORDER BY id DESC";

        try (Connection conn = conexion.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario != null ? usuario : "admin");
            if (placaBusqueda != null && !placaBusqueda.trim().isEmpty()) {
                pstmt.setString(2, "%" + placaBusqueda.trim().toUpperCase() + "%");
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Vehiculo v = VehiculoFactory.crearVehiculo(
                    rs.getInt("id"),
                    rs.getString("placa"),
                    rs.getString("marca"),
                    rs.getString("modelo"),
                    rs.getString("tipo"),
                    rs.getString("hora_entrada"),
                    rs.getString("usuario")
                );
                lista.add(v);
            }
        } catch (Exception e) {
            System.err.println("Error al obtener vehículos para " + usuario + ": " + e.getMessage());
        }
        return lista;
    }

    public void registrarVehiculo(Vehiculo vehiculo, String usuario) {
        String sql = "INSERT INTO vehiculos (placa, marca, modelo, tipo, hora_entrada, usuario) VALUES (?, ?, ?, ?, ?, ?)";
        String ahora = LocalDateTime.now().format(FMT);

        try (Connection conn = conexion.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, vehiculo.getPlaca() != null ? vehiculo.getPlaca().toUpperCase().trim() : "");
            pstmt.setString(2, vehiculo.getMarca() != null ? vehiculo.getMarca().toUpperCase().trim() : "");
            pstmt.setString(3, vehiculo.getModelo() != null ? vehiculo.getModelo().toUpperCase().trim() : "");
            pstmt.setString(4, vehiculo.getTipo() != null ? vehiculo.getTipo() : "CARRO");
            pstmt.setString(5, ahora);
            pstmt.setString(6, usuario != null ? usuario : "admin");
            pstmt.executeUpdate();
            System.out.println("✅ ¡Vehículo guardado exitosamente para el usuario " + usuario + "!");
        } catch (Exception e) {
            System.err.println("❌ ERROR al registrar vehículo: " + e.getMessage());
        }
    }

    public void eliminarVehiculo(int id, String usuario) {
        String sql = "DELETE FROM vehiculos WHERE id = ? AND usuario = ?";

        try (Connection conn = conexion.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, usuario != null ? usuario : "admin");
            pstmt.executeUpdate();
            System.out.println("🗑️ ¡Vehículo cancelado de la BD para el usuario " + usuario + "!");
        } catch (Exception e) {
            System.err.println("❌ ERROR al eliminar vehículo: " + e.getMessage());
        }
    }

    public Vehiculo obtenerVehiculoPorId(int id, String usuario) {
        String sql = "SELECT * FROM vehiculos WHERE id = ? AND usuario = ?";

        try (Connection conn = conexion.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, usuario != null ? usuario : "admin");
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return VehiculoFactory.crearVehiculo(
                    rs.getInt("id"),
                    rs.getString("placa"),
                    rs.getString("marca"),
                    rs.getString("modelo"),
                    rs.getString("tipo"),
                    rs.getString("hora_entrada"),
                    rs.getString("usuario")
                );
            }
        } catch (Exception e) {
            System.err.println("❌ ERROR al buscar vehículo por ID: " + e.getMessage());
        }
        return null;
    }

    public boolean procesarSalida(TicketCobro ticket, String usuario) {
        String sqlInsert = "INSERT INTO historial_salidas (placa, marca, modelo, tipo, hora_entrada, hora_salida, minutos_totales, total_pagado, usuario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlDelete = "DELETE FROM vehiculos WHERE id = ? AND usuario = ?";

        try (Connection conn = conexion.conectar()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert);
                 PreparedStatement pstmtDelete = conn.prepareStatement(sqlDelete)) {

                pstmtInsert.setString(1, ticket.getPlaca());
                pstmtInsert.setString(2, ticket.getMarca());
                pstmtInsert.setString(3, ticket.getModelo());
                pstmtInsert.setString(4, ticket.getTipo());
                pstmtInsert.setString(5, ticket.getHoraEntrada());
                pstmtInsert.setString(6, ticket.getHoraSalida());
                pstmtInsert.setLong(7, ticket.getMinutosTotales());
                pstmtInsert.setDouble(8, ticket.getTotalPagar());
                pstmtInsert.setString(9, usuario != null ? usuario : "admin");
                pstmtInsert.executeUpdate();

                pstmtDelete.setInt(1, ticket.getVehiculoId());
                pstmtDelete.setString(2, usuario != null ? usuario : "admin");
                pstmtDelete.executeUpdate();

                conn.commit();
                System.out.println("✅ Salida y cobro procesados para placa: " + ticket.getPlaca() + " (Usuario: " + usuario + ")");
                return true;
            } catch (Exception e) {
                conn.rollback();
                System.err.println("❌ ERROR al procesar salida: " + e.getMessage());
                return false;
            }
        } catch (Exception e) {
            System.err.println("❌ ERROR en conexión al procesar salida: " + e.getMessage());
            return false;
        }
    }

    public List<HistorialSalida> obtenerHistorial(String usuario) {
        List<HistorialSalida> historial = new ArrayList<>();
        String sql = "SELECT * FROM historial_salidas WHERE usuario = ? ORDER BY id DESC";

        try (Connection conn = conexion.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario != null ? usuario : "admin");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                HistorialSalida h = new HistorialSalida();
                h.setId(rs.getInt("id"));
                h.setPlaca(rs.getString("placa"));
                h.setMarca(rs.getString("marca"));
                h.setModelo(rs.getString("modelo"));
                h.setTipo(rs.getString("tipo"));
                h.setHoraEntrada(rs.getString("hora_entrada"));
                h.setHoraSalida(rs.getString("hora_salida"));
                h.setMinutosTotales(rs.getLong("minutos_totales"));
                h.setTotalPagado(rs.getDouble("total_pagado"));
                h.setUsuario(rs.getString("usuario"));
                historial.add(h);
            }
        } catch (Exception e) {
            System.err.println("❌ Error al obtener historial para " + usuario + ": " + e.getMessage());
        }
        return historial;
    }

    public double obtenerTotalRecaudado(String usuario) {
        String sql = "SELECT COALESCE(SUM(total_pagado), 0) AS total FROM historial_salidas WHERE usuario = ?";

        try (Connection conn = conexion.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario != null ? usuario : "admin");
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (Exception e) {
            System.err.println("❌ Error al calcular total recaudado: " + e.getMessage());
        }
        return 0.0;
    }
}
