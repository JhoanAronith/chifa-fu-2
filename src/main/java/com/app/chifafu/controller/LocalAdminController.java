package com.app.chifafu.controller;

import com.app.chifafu.model.Local;
import com.app.chifafu.service.LocalService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/chifafu/admin/locales")
public class LocalAdminController {

    private final LocalService localService;

    public LocalAdminController(LocalService localService) {
        this.localService = localService;
    }

    //listar todos los locales
    @GetMapping
    public String listarLocales(Model model) {
        model.addAttribute("locales", localService.listarLocales());
        return "administrador/locales/lista"; // vista thymeleaf
    }

    // FORMULARIO: REGISTRAR
    @GetMapping("/formulario")
    public String nuevoLocalForm(Model model) {
        model.addAttribute("local", new Local());
        return "administrador/locales/formulario";
    }

    // GUARDAR NUEVO
    @PostMapping("/guardar")
    public String guardarLocal(@ModelAttribute("local") Local local,
                               RedirectAttributes redirect) {

        localService.crearLocal(local);
        redirect.addFlashAttribute("success", "Local registrado correctamente");
        return "redirect:/chifafu/admin/locales";
    }

    // FORMULARIO: EDITAR
    @GetMapping("/editar/{id}")
    public String editarLocalForm(@PathVariable Long id, Model model,
                                  RedirectAttributes redirect) {

        Optional<Local> localOpt = localService.obtenerPorId(id);

        if (localOpt.isEmpty()) {
            redirect.addFlashAttribute("error", "El local no existe");
            return "redirect:/chifafu/admin/locales";
        }

        model.addAttribute("local", localOpt.get());
        return "administrador/locales/editar";
    }

    // ACTUALIZAR
    @PostMapping("/actualizar")
    public String actualizarLocal(@ModelAttribute("local") Local local,
                                  RedirectAttributes redirect) {

        localService.actualizarLocal(local);
        redirect.addFlashAttribute("success", "Local actualizado correctamente");
        return "redirect:/chifafu/admin/locales";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminarLocal(@PathVariable Long id, RedirectAttributes redirect) {
        int filas = localService.eliminarLocal(id);

        if (filas > 0)
            redirect.addFlashAttribute("success", "Local eliminado");
        else
            redirect.addFlashAttribute("error", "No se pudo eliminar");

        return "redirect:/chifafu/admin/locales";
    }

    // ACTIVAR
    @GetMapping("/activar/{id}")
    public String activar(@PathVariable Long id, RedirectAttributes redirect) {
        localService.activarLocal(id);
        redirect.addFlashAttribute("success", "Local activado");
        return "redirect:/chifafu/admin/locales";
    }

    // DESACTIVAR
    @GetMapping("/desactivar/{id}")
    public String desactivar(@PathVariable Long id, RedirectAttributes redirect) {
        localService.desactivarLocal(id);
        redirect.addFlashAttribute("success", "Local desactivado");
        return "redirect:/chifafu/admin/locales";
    }

}
