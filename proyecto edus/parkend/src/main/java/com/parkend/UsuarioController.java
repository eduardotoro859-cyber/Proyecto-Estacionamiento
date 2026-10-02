package com.parkend;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    private String obtenerUsuarioActivo(HttpSession session) {
        if (session != null && session.getAttribute("usuarioLogueado") != null) {
            return (String) session.getAttribute("usuarioLogueado");
        }
        return "admin";
    }

    @GetMapping("/usuarios")
    public String listarUsuarios(HttpSession session, Model model) {
        String usuarioActivo = obtenerUsuarioActivo(session);
        model.addAttribute("usuarioActivo", usuarioActivo);
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        model.addAttribute("totalUsuarios", usuarioService.contarUsuarios());
        return "usuarios";
    }

    @PostMapping("/usuarios/crear")
    public String crearUsuario(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               @RequestParam(name = "confirmPassword", required = false) String confirmPassword,
                               RedirectAttributes redirectAttributes) {
        if (username == null || username.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajeError", "El nombre de usuario no puede estar vacío.");
            return "redirect:/usuarios";
        }

        if (password == null || password.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajeError", "La contraseña no puede estar vacía.");
            return "redirect:/usuarios";
        }

        if (confirmPassword != null && !password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("mensajeError", "Las contraseñas no coinciden. Inténtalo de nuevo.");
            return "redirect:/usuarios";
        }

        if (usuarioService.existeUsuario(username.trim())) {
            redirectAttributes.addFlashAttribute("mensajeError", "El usuario '" + username.trim() + "' ya existe en el sistema.");
            return "redirect:/usuarios";
        }

        boolean registrado = usuarioService.registrarUsuario(username.trim(), password);
        if (registrado) {
            redirectAttributes.addFlashAttribute("mensajeExito", "Usuario '" + username.trim() + "' creado exitosamente.");
        } else {
            redirectAttributes.addFlashAttribute("mensajeError", "Ocurrió un error al registrar el usuario.");
        }

        return "redirect:/usuarios";
    }

    @PostMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable("id") int id,
                                  @RequestParam(name = "confirmacion", required = false, defaultValue = "") String confirmacion,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        String usuarioActivo = obtenerUsuarioActivo(session);
        String error = usuarioService.eliminarUsuario(id, usuarioActivo, confirmacion);

        if (error != null) {
            redirectAttributes.addFlashAttribute("mensajeError", error);
        } else {
            redirectAttributes.addFlashAttribute("mensajeExito", "El usuario ha sido eliminado correctamente del sistema.");
        }

        return "redirect:/usuarios";
    }
}
