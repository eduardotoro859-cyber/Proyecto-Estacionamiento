# 🚗 Parkend Pro - Sistema de Control y Gestión de Estacionamiento

Sistema desarrollado íntegramente en **Java Puro (Java 17)** bajo el paradigma de **Programación Orientada a Objetos (POO)**, arquitectura **MVC (Modelo-Vista-Controlador)** con **Thymeleaf**, interfaz web moderna con **Bootstrap 5**, y persistencia relacional con **SQLite** a través de **JDBC estándar**.

> **Nota de Evaluación Académica:**
> Este proyecto utiliza **100% Java Puro**:
> - ✅ **0% JSON**: No se utiliza formato JSON en almacenamiento ni en comunicación. No existen archivos `.json` en el proyecto ni librerías de serialización JSON.
> - ✅ **Persistencia Relacional Nativa:** Conexión mediante `java.sql.*` (JDBC) a base de datos relacional SQLite (`parkend.db`).
> - ✅ **POO Avanzada:** Herencia (`Vehiculo`), Polimorfismo (`Carro`, `Moto`, `Camion`), Encapsulamiento y Patrón de Diseño Creacional (`VehiculoFactory`).
> - ✅ **Arquitectura MVC Clásica:** Controladores Spring MVC estándar que gestionan modelos Java (`Model`, `RedirectAttributes`) y devuelven vistas HTML procesadas en servidor.

---

## 📋 Requisitos Previos en el Nuevo Equipo

1. **Java JDK 17 o superior**:
   - Descarga recomendada: [Eclipse Adoptium Temurin JDK 17](https://adoptium.net/temurin/releases/?version=17) u Oracle JDK 17+.
   - Durante la instalación en Windows, asegúrate de marcar:
     - *"Add to PATH"*
     - *"Set JAVA_HOME variable"*

2. **Visual Studio Code**:
   - Descarga: [https://code.visualstudio.com/](https://code.visualstudio.com/)
   - Extensión recomendada: **Extension Pack for Java** (`vscjava.vscode-java-pack`).

---

## 🚀 Cómo Ejecutar en Visual Studio Code

### Método 1: Desde VS Code (Botón Run Java)
1. Abre **Visual Studio Code**.
2. Ve a: `Archivo` -> `Abrir carpeta...` y selecciona la carpeta raíz del proyecto (`proyecto edus`).
3. Abre el archivo `parkend/src/main/java/com/parkend/Main.java`.
4. Haz clic en el botón de reproducir **Run Java** (▷) en la esquina superior derecha, o haz clic en **Run** encima del método `main`.
5. Abre en tu navegador: **[http://localhost:8080](http://localhost:8080)**

---

### Método 2: Doble clic en `ejecutar.bat`
1. Haz doble clic en el archivo **`ejecutar.bat`** en la carpeta principal.
2. El script detecta automáticamente el entorno Java y levanta el servidor.
3. Abre en tu navegador: **[http://localhost:8080](http://localhost:8080)**

---

### Método 3: Por Terminal
Desde la carpeta raíz del proyecto:
```bash
.\mvnw.cmd compile
.\mvnw.cmd spring-boot:run -pl parkend
```

---

## 🔑 Cuentas de Acceso Preconfiguradas

| Usuario | Contraseña | Rol / Descripción |
| :--- | :--- | :--- |
| **`admin`** | `admin123` | Administrador general predeterminado |
| **`Edujost3`** | *(clave configurada)* | Operador registrado |
| **`carlos`** | *(clave configurada)* | Operador registrado |
| **`maria`** | *(clave configurada)* | Operadora registrada |

---

## 🏛️ Principios de POO y Arquitectura Aplicados

1. **Polimorfismo y Herencia:**
   - Clase abstracta base `Vehiculo` con métodos abstractos `getTarifaPorHora()` y `getDescripcionTipo()`.
   - Subclases especializadas: `Carro`, `Moto`, `Camion`, cada una implementando su lógica de cobro polimórfica.

2. **Patrón de Diseño Factory:**
   - `VehiculoFactory`: Centraliza la creación e instanciación segura de objetos derivados según el tipo de vehículo.

3. **Capa de Persistencia JDBC:**
   - `ConexionRepository`, `VehiculoRepository`, `UsuarioRepository`: Acceso directo a base de datos usando `PreparedStatement`, `Statement` y `ResultSet` estándar de Java (`java.sql`).

4. **Seguridad y Encriptación en Java:**
   - Algoritmo de hash criptográfico `SHA-256` implementado nativamente con `java.security.MessageDigest` para el almacenamiento seguro de contraseñas.
