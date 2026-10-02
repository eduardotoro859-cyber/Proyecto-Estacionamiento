package com.parkend;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class VehiculoController {

    @Autowired
    private VehiculoService vehiculoService;

    private String obtenerUsuarioActivo(HttpSession session) {
        if (session != null && session.getAttribute("usuarioLogueado") != null) {
            return (String) session.getAttribute("usuarioLogueado");
        }
        return "admin";
    }

    private double obtenerTotalSesion(HttpSession session) {
        if (session == null) return 0.0;
        Double total = (Double) session.getAttribute("totalRecaudadoSesion");
        if (total == null) {
            total = 0.0;
            session.setAttribute("totalRecaudadoSesion", total);
        }
        return total;
    }

    @GetMapping("/vehiculos")
    public String listarVehiculos(@RequestParam(name = "placa", required = false) String placa,
                                  HttpSession session,
                                  Model model) {
        String usuario = obtenerUsuarioActivo(session);
        model.addAttribute("usuarioActivo", usuario);
        model.addAttribute("vehiculos", vehiculoService.listarVehiculos(usuario, placa));
        model.addAttribute("vehiculoForm", new VehiculoForm());
        model.addAttribute("totalRecaudado", obtenerTotalSesion(session)); // Inicia en 0 en cada nueva sesión
        model.addAttribute("totalHistorico", vehiculoService.obtenerTotalRecaudado(usuario));
        return "vehiculos";
    }

    @PostMapping("/vehiculos")
    public String registrarIngreso(@ModelAttribute VehiculoForm form,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        String usuario = obtenerUsuarioActivo(session);
        vehiculoService.registrarIngreso(form, usuario);
        Vehiculo v = VehiculoFactory.crearVehiculo(form.getTipo());
        redirectAttributes.addFlashAttribute("mensajeExito",
            "¡Ingreso registrado! " + v.getDescripcionTipo() + " con placa " + (form.getPlaca() != null ? form.getPlaca().toUpperCase() : ""));
        return "redirect:/vehiculos";
    }

    @GetMapping("/vehiculos/cobrar/{id}")
    public String mostrarTicketCobro(@PathVariable("id") int id,
                                     HttpSession session,
                                     Model model,
                                     RedirectAttributes redirectAttributes) {
        String usuario = obtenerUsuarioActivo(session);
        TicketCobro ticket = vehiculoService.generarTicket(id, usuario);
        if (ticket == null) {
            redirectAttributes.addFlashAttribute("mensajeError", "El vehículo no fue encontrado o ya fue retirado.");
            return "redirect:/vehiculos";
        }
        model.addAttribute("ticket", ticket);
        model.addAttribute("usuarioActivo", usuario);
        return "cobro";
    }

    @PostMapping("/vehiculos/cobrar")
    public String procesarCobro(@ModelAttribute TicketCobro ticket,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        String usuario = obtenerUsuarioActivo(session);
        ticket.setUsuario(usuario);
        boolean exito = vehiculoService.procesarSalida(ticket, usuario);
        if (exito) {
            // Actualizar total recaudado en la sesión activa
            double actual = obtenerTotalSesion(session);
            actual += ticket.getTotalPagar();
            session.setAttribute("totalRecaudadoSesion", actual);

            redirectAttributes.addFlashAttribute("mensajeExito",
                "✅ Salida procesada — Placa: " + ticket.getPlaca() +
                " — Total cobrado: $" + String.format(java.util.Locale.US, "%.2f", ticket.getTotalPagar()));
        } else {
            redirectAttributes.addFlashAttribute("mensajeError", "Ocurrió un error al procesar la salida.");
        }
        return "redirect:/vehiculos";
    }

    @GetMapping("/vehiculos/historial")
    public String verHistorial(HttpSession session, Model model) {
        String usuario = obtenerUsuarioActivo(session);
        model.addAttribute("usuarioActivo", usuario);
        model.addAttribute("historial", vehiculoService.obtenerHistorial(usuario));
        model.addAttribute("totalRecaudado", obtenerTotalSesion(session)); // Sesión actual
        model.addAttribute("totalHistorico", vehiculoService.obtenerTotalRecaudado(usuario)); // Acumulado histórico
        return "historial";
    }

    @GetMapping("/vehiculos/eliminar/{id}")
    public String cancelarIngreso(@PathVariable("id") int id,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        String usuario = obtenerUsuarioActivo(session);
        vehiculoService.cancelarIngreso(id, usuario);
        redirectAttributes.addFlashAttribute("mensajeExito", "Ingreso cancelado correctamente.");
        return "redirect:/vehiculos";
    }
}
