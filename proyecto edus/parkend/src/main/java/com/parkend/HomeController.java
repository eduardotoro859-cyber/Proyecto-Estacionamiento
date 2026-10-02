package com.parkend;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String inicio(HttpSession session) {
        if (session != null && session.getAttribute("usuarioLogueado") != null) {
            return "redirect:/vehiculos";
        }
        return "redirect:/login"; // Redirige directamente al login si no hay sesión
    }
}
