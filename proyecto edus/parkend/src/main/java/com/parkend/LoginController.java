package com.parkend;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String mostrarLogin(HttpSession session) {
        if (session != null && session.getAttribute("usuarioLogueado") != null) {
            return "redirect:/vehiculos";
        }
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam("username") String username,
                                @RequestParam("password") String password,
                                HttpSession session,
                                Model model) {
        if (usuarioService.validarCredenciales(username, password)) {
            session.setAttribute("usuarioLogueado", username);
            session.setAttribute("totalRecaudadoSesion", 0.0);
            return "redirect:/vehiculos";
        } else {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
            return "login";
        }
    }

    @GetMapping("/registro-usuario")
    public String mostrarRegistroUsuario(HttpSession session) {
        if (session != null && session.getAttribute("usuarioLogueado") != null) {
            return "redirect:/vehiculos";
        }
        return "registro-usuario";
    }

    @PostMapping("/registro-usuario")
    public String procesarRegistroUsuario(@RequestParam("username") String username,
                                          @RequestParam("password") String password,
                                          Model model) {
        boolean registrado = usuarioService.registrarUsuario(username, password);
        if (registrado) {
            model.addAttribute("exito", "¡Registro exitoso! Ahora puedes iniciar sesión.");
            return "login";
        } else {
            model.addAttribute("error", "El usuario ya existe o hubo un error.");
            return "registro-usuario";
        }
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login?logout";
    }
}
