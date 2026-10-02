@echo off
setlocal enabledelayedexpansion
title Parkend Pro - Servidor de Estacionamiento
echo ========================================================
echo        Iniciando Parkend Pro - Servidor Web
echo ========================================================

:: 1. Si JAVA_HOME esta definido pero la ruta no existe, descartarlo
if defined JAVA_HOME (
    if not exist "%JAVA_HOME%\bin\java.exe" (
        echo [!] JAVA_HOME apuntaba a una ruta inexistente: %JAVA_HOME%
        echo     Buscando Java en el sistema...
        set "JAVA_HOME="
    )
)

:: 2. Si no hay JAVA_HOME valido, comprobar si 'java' esta en el PATH
if not defined JAVA_HOME (
    where java >nul 2>nul
    if !errorlevel! equ 0 (
        echo [*] Java detectado en el PATH del sistema.
    ) else (
        :: 3. Buscar en rutas estandar comunes de Windows (JDK 17+)
        for /d %%D in ("%ProgramFiles%\Eclipse Adoptium\jdk-17*" "%ProgramFiles%\Java\jdk-17*" "%ProgramFiles%\Java\jdk-21*" "%LOCALAPPDATA%\Programs\Eclipse Adoptium\jdk-17*") do (
            if exist "%%D\bin\java.exe" (
                set "JAVA_HOME=%%D"
            )
        )
    )
)

:: 4. Validar disponibilidad de Java
if defined JAVA_HOME (
    echo [1/2] Entorno Java configurado en:
    echo       %JAVA_HOME%
) else (
    where java >nul 2>nul
    if !errorlevel! neq 0 (
        echo.
        echo [ERROR] No se encontro Java instalado en este equipo.
        echo Se requiere Java 17 o superior para ejecutar Parkend Pro.
        echo Por favor descarga e instala Java 17 desde:
        echo https://adoptium.net/temurin/releases/?version=17
        echo.
        pause
        exit /b 1
    )
    echo [1/2] Usando Java desde el PATH del sistema.
)
echo.

cd /d "%~dp0"
if exist "parkend" cd parkend

echo [2/2] Levantando servidor en http://localhost:8080 ...
echo       Abre http://localhost:8080 en tu navegador web.
echo.
call mvnw.cmd spring-boot:run
if !errorlevel! neq 0 (
    echo.
    echo [AVISO] El proceso del servidor finalizo o se detuvo.
)
pause
