package com.parkend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public boolean validarCredenciales(String username, String password) {
        return usuarioRepository.validarUsuario(username, password);
    }

    public boolean registrarUsuario(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return false;
        }
        if (usuarioRepository.obtenerPorUsername(username.trim()) != null) {
            return false;
        }
        return usuarioRepository.registrarUsuario(username.trim(), password);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.listarUsuarios();
    }

    public Usuario obtenerPorId(int id) {
        return usuarioRepository.obtenerPorId(id);
    }

    public int contarUsuarios() {
        return usuarioRepository.contarUsuarios();
    }

    public boolean existeUsuario(String username) {
        return usuarioRepository.obtenerPorUsername(username) != null;
    }

    /**
     * Elimina un usuario con verificación estricta de palabra de confirmación ("delete")
     * y reglas de seguridad de sesión.
     * @return null si fue exitoso, o el mensaje de error correspondiente.
     */
    public String eliminarUsuario(int id, String usuarioLogueado, String confirmacion) {
        if (confirmacion == null || !confirmacion.trim().equalsIgnoreCase("delete")) {
            return "Acción cancelada: Debes escribir la palabra exacta 'delete' para confirmar la eliminación.";
        }

        Usuario usuario = usuarioRepository.obtenerPorId(id);
        if (usuario == null) {
            return "El usuario solicitado no existe o ya fue eliminado.";
        }

        if (usuarioLogueado != null && usuario.getUsername().equalsIgnoreCase(usuarioLogueado.trim())) {
            return "Seguridad: No puedes eliminar la cuenta con la que tienes la sesión activa.";
        }

        if (usuarioRepository.contarUsuarios() <= 1) {
            return "Seguridad: No es posible eliminar el último usuario registrado en el sistema.";
        }

        boolean eliminado = usuarioRepository.eliminarUsuario(id);
        if (!eliminado) {
            return "No se pudo eliminar el usuario de la base de datos.";
        }

        return null; // Éxito
    }
}
