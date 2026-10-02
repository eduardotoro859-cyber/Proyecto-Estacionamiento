package com.parkend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UsuarioRepository {

    @Autowired
    private ConexionRepository conexion;

    /**
     * Aplica función hash criptográfica SHA-256 a la contraseña.
     */
    public static String hashPassword(String password) {
        if (password == null) return "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            return password;
        }
    }

    public boolean validarUsuario(String username, String password) {
        String sql = "SELECT * FROM usuarios WHERE username = ? AND (password = ? OR password = ?)";
        String hashed = hashPassword(password);

        try (Connection conn = conexion.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username != null ? username.trim() : "");
            pstmt.setString(2, hashed);
            pstmt.setString(3, password); // Compatibilidad retroactiva con claves en texto plano preexistentes

            ResultSet rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.err.println("❌ Error al validar el usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean registrarUsuario(String username, String password) {
        String sql = "INSERT INTO usuarios (username, password) VALUES (?, ?)";
        String hashed = hashPassword(password);

        try (Connection conn = conexion.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username != null ? username.trim() : "");
            pstmt.setString(2, hashed);
            pstmt.executeUpdate();
            System.out.println("👤 Nuevo usuario registrado con éxito (clave cifrada): " + username);
            return true;

        } catch (SQLException e) {
            System.err.println("❌ Error al registrar el usuario: " + e.getMessage());
            return false;
        }
    }

    public List<Usuario> listarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT id, username FROM usuarios ORDER BY id ASC";

        try (Connection conn = conexion.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                usuarios.add(new Usuario(rs.getInt("id"), rs.getString("username")));
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al listar usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    public Usuario obtenerPorId(int id) {
        String sql = "SELECT id, username FROM usuarios WHERE id = ?";

        try (Connection conn = conexion.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Usuario(rs.getInt("id"), rs.getString("username"));
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener usuario por ID: " + e.getMessage());
        }
        return null;
    }

    public Usuario obtenerPorUsername(String username) {
        String sql = "SELECT id, username FROM usuarios WHERE LOWER(username) = LOWER(?)";

        try (Connection conn = conexion.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username != null ? username.trim() : "");
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Usuario(rs.getInt("id"), rs.getString("username"));
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener usuario por username: " + e.getMessage());
        }
        return null;
    }

    public int contarUsuarios() {
        String sql = "SELECT COUNT(*) AS total FROM usuarios";

        try (Connection conn = conexion.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al contar usuarios: " + e.getMessage());
        }
        return 0;
    }

    public boolean eliminarUsuario(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (Connection conn = conexion.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }
}
