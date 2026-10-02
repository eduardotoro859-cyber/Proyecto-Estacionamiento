package com.parkend;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@Repository
public class ConexionRepository {

    public static String getDatabaseUrl() {
        String customPath = System.getProperty("db.path");
        if (customPath != null && !customPath.isBlank()) {
            return "jdbc:sqlite:" + customPath;
        }

        // Si se ejecuta desde la raíz del proyecto (ej: abriendo la carpeta principal en VS Code)
        File subfolderDb = new File("parkend", "parkend.db");
        if (subfolderDb.exists()) {
            return "jdbc:sqlite:" + subfolderDb.getAbsolutePath();
        }

        File subfolder = new File("parkend");
        if (subfolder.isDirectory()) {
            return "jdbc:sqlite:" + subfolderDb.getAbsolutePath();
        }

        // Si se ejecuta directamente dentro del subdirectorio 'parkend'
        File currentDb = new File("parkend.db");
        return "jdbc:sqlite:" + currentDb.getAbsolutePath();
    }

    public Connection conectar() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(getDatabaseUrl());
        } catch (Exception e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return conn;
    }

    @PostConstruct
    public void inicializarBaseDatos() {
        String sqlVehiculos = "CREATE TABLE IF NOT EXISTS vehiculos ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "placa TEXT NOT NULL, "
                + "marca TEXT NOT NULL, "
                + "modelo TEXT NOT NULL, "
                + "tipo TEXT NOT NULL, "
                + "hora_entrada TEXT NOT NULL, "
                + "usuario TEXT NOT NULL DEFAULT 'admin');";

        String sqlUsuarios = "CREATE TABLE IF NOT EXISTS usuarios ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT UNIQUE NOT NULL, "
                + "password TEXT NOT NULL);";

        String sqlHistorial = "CREATE TABLE IF NOT EXISTS historial_salidas ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "placa TEXT NOT NULL, "
                + "marca TEXT NOT NULL, "
                + "modelo TEXT NOT NULL, "
                + "tipo TEXT NOT NULL, "
                + "hora_entrada TEXT NOT NULL, "
                + "hora_salida TEXT NOT NULL, "
                + "minutos_totales INTEGER NOT NULL, "
                + "total_pagado REAL NOT NULL, "
                + "usuario TEXT NOT NULL DEFAULT 'admin');";

        try (Connection conn = conectar();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(sqlVehiculos);
            stmt.execute(sqlUsuarios);
            stmt.execute(sqlHistorial);

            // Migración defensiva: asegurar columna usuario en bases de datos ya creadas
            try {
                stmt.execute("ALTER TABLE vehiculos ADD COLUMN usuario TEXT NOT NULL DEFAULT 'admin';");
            } catch (Exception ignored) {
                // Columna ya existe
            }
            try {
                stmt.execute("ALTER TABLE historial_salidas ADD COLUMN usuario TEXT NOT NULL DEFAULT 'admin';");
            } catch (Exception ignored) {
                // Columna ya existe
            }

            // Crear usuario administrador por defecto si la tabla está vacía
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM usuarios");
            if (rs.next() && rs.getInt("total") == 0) {
                stmt.execute("INSERT INTO usuarios (username, password) VALUES ('admin', 'admin123');");
                System.out.println("👤 Usuario 'admin' (clave: 'admin123') creado por defecto.");
            }

            System.out.println("✅ Base de datos inicializada correctamente en @PostConstruct.");
        } catch (Exception e) {
            System.err.println("Error al inicializar la base de datos: " + e.getMessage());
        }
    }
}
