package com.app.chifafu.controller;

import com.app.chifafu.model.Categoria;
import com.app.chifafu.service.CategoriaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/chifafu/admin/categorias")
public class CategoriaAdminController {

    private final CategoriaService categoriaService;

    public CategoriaAdminController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // Listar todas las categorías
    @GetMapping
    public String listarCategorias(Model model) {
        List<Categoria> categorias = categoriaService.listarCategorias();
        model.addAttribute("categorias", categorias);
        return "administrador/categorias/lista";
    }

    // Mostrar formulario para crear nueva categoría
    @GetMapping("/nueva")
    public String mostrarFormularioNueva(Model model) {
        model.addAttribute("categoria", new Categoria());
        model.addAttribute("accion", "Crear");
        return "administrador/categorias/formulario";
    }

    // Guardar nueva categoría
    @PostMapping("/guardar")
    public String guardarCategoria(@ModelAttribute("categoria") Categoria categoria,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "administrador/categorias/formulario";
        }
        try {
            categoriaService.crearCategoria(categoria);
            redirectAttributes.addFlashAttribute("mensaje", "Categoría creada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al crear la categoría: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/chifafu/admin/categorias";
    }

    // Mostrar formulario para editar categoría
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Categoria> categoriaOpt = categoriaService.obtenerPorId(id);

        if (categoriaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensaje", "Categoría no encontrada");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/chifafu/admin/categorias";
        }

        model.addAttribute("categoria", categoriaOpt.get());
        model.addAttribute("accion", "Editar");
        return "administrador/categorias/formulario";
    }

    // Actualizar categoría
    @PostMapping("/actualizar")
    public String actualizarCategoria(@ModelAttribute("categoria") Categoria categoria,
                                      BindingResult result,
                                      RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "administrador/categorias/formulario";
        }

        try {
            categoriaService.actualizarCategoria(categoria);
            redirectAttributes.addFlashAttribute("mensaje", "Categoría actualizada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al actualizar la categoría: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }

        return "redirect:/chifafu/admin/categorias";
    }

    // Eliminar categoría
    @GetMapping("/eliminar/{id}")
    public String eliminarCategoria(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            int resultado = categoriaService.eliminarCategoria(id);
            if (resultado > 0) {
                redirectAttributes.addFlashAttribute("mensaje", "Categoría eliminada exitosamente");
                redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            } else {
                redirectAttributes.addFlashAttribute("mensaje", "No se pudo eliminar la categoría");
                redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar la categoría: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }

        return "redirect:/chifafu/admin/categorias";
    }

    // Activar categoría
    @GetMapping("/activar/{id}")
    public String activarCategoria(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoriaService.activarCategoria(id);
            redirectAttributes.addFlashAttribute("mensaje", "Categoría activada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al activar la categoría: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }

        return "redirect:/chifafu/admin/categorias";
    }

    // Desactivar categoría
    @GetMapping("/desactivar/{id}")
    public String desactivarCategoria(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoriaService.desactivarCategoria(id);
            redirectAttributes.addFlashAttribute("mensaje", "Categoría desactivada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al desactivar la categoría: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/chifafu/admin/categorias";
    }
}