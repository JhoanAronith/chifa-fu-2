package com.app.chifafu.controller;

import com.app.chifafu.dto.MenuDTO;
import com.app.chifafu.model.Categoria;
import com.app.chifafu.model.Menu;
import com.app.chifafu.service.CategoriaService;
import com.app.chifafu.service.MenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/chifafu/admin/menu")
public class MenuAdminController {

    private final MenuService menuService;
    private final CategoriaService categoriaService;

    public MenuAdminController(MenuService menuService, CategoriaService categoriaService) {
        this.menuService = menuService;
        this.categoriaService = categoriaService;
    }

    // Listar todos los platos del menú
    @GetMapping
    public String listarMenu(Model model) {
        List<Menu> menus = menuService.listarMenus();
        List<Categoria> categorias = categoriaService.listarCategorias();
        model.addAttribute("menus", menus);
        model.addAttribute("categorias", categorias);
        return "administrador/menu/lista";
    }

    // Mostrar formulario para crear nuevo plato
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        List<Categoria> categorias = categoriaService.listarActivas();
        model.addAttribute("menuDTO", new MenuDTO());
        model.addAttribute("categorias", categorias);
        model.addAttribute("accion", "Crear");
        return "administrador/menu/formulario";
    }

    //guardar nuevo plato
    @PostMapping("/guardar")
    public String guardarMenu(@ModelAttribute("menuDTO") MenuDTO menuDTO,
                              BindingResult result,
                              RedirectAttributes redirectAttributes,
                              Model model,
                              @RequestParam("categoriaId") Long categoriaId) {

        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaService.listarActivas());
            model.addAttribute("accion", "Crear");
            return "administrador/menu/formulario";
        }

        try {
            Optional<Categoria> categoriaOpt = categoriaService.obtenerPorId(categoriaId);
            if (categoriaOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("mensaje", "Categoría no encontrada");
                redirectAttributes.addFlashAttribute("tipoMensaje", "error");
                return "redirect:/chifafu/admin/menu/nuevo";
            }

            // Convertir DTO → entidad
            Menu menu = new Menu();
            menu.setId_menu(menuDTO.getId_menu());
            menu.setNombre(menuDTO.getNombre());
            menu.setDescripcion(menuDTO.getDescripcion());
            menu.setPrecio(menuDTO.getPrecio());
            menu.setTiempo_preparacion(menuDTO.getTiempo_preparacion());
            menu.setStock(menuDTO.getStock());
            menu.setOrden(menuDTO.getOrden());
            menu.setCantidad_vendida(0);
            menu.setTotal_ingresos(0.0);

            menu.setCategoria(categoriaOpt.get());

            menuService.crearMenu(menu);

            redirectAttributes.addFlashAttribute("mensaje", "Plato creado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al crear el plato: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }

        return "redirect:/chifafu/admin/menu";
    }

    // Mostrar formulario para editar plato
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id,
                                          Model model,
                                          RedirectAttributes redirectAttributes) {
        Optional<Menu> menuOpt = menuService.obtenerPorId(id);

        if (menuOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensaje", "Plato no encontrado");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/chifafu/admin/menu";
        }

        Menu menu = menuOpt.get();

        MenuDTO dto = new MenuDTO();
        dto.setId_menu(menu.getId_menu());
        dto.setNombre(menu.getNombre());
        dto.setDescripcion(menu.getDescripcion());
        dto.setPrecio(menu.getPrecio());
        dto.setRuta_imagen(menu.getRuta_imagen());
        dto.setDisponible(menu.getDisponible());
        dto.setOrden(menu.getOrden());
        dto.setTiempo_preparacion(menu.getTiempo_preparacion());
        dto.setDisponible_delivery(menu.getDisponible_delivery());
        dto.setDisponible_local(menu.getDisponible_local());
        dto.setEs_recomendado(menu.getEs_recomendado());
        dto.setEs_nuevo(menu.getEs_nuevo());
        dto.setEs_popular(menu.getEs_popular());
        dto.setRequiere_inventario(menu.getRequiere_inventario());
        dto.setStock(menu.getStock());
        dto.setCategoriaId(menu.getCategoria().getId_categoria());
        dto.setEn_promocion(menu.getEn_promocion());
        dto.setPrecio_promocion(menu.getPrecio_promocion());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (menu.getFecha_inicio_promocion() != null)
            dto.setFecha_inicio_promocion(sdf.format(menu.getFecha_inicio_promocion()));

        if (menu.getFecha_fin_promocion() != null)
            dto.setFecha_fin_promocion(sdf.format(menu.getFecha_fin_promocion()));

        List<Categoria> categorias = categoriaService.listarActivas();

        model.addAttribute("menuDTO", dto);
        model.addAttribute("categorias", categorias);
        model.addAttribute("accion", "Editar");

        return "administrador/menu/formulario";
    }

    //Editar un menú
    @PostMapping("/actualizar")
    public String actualizarMenu(@ModelAttribute("menuDTO") MenuDTO dto,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        try {
            Menu menu = new Menu();
            menu.setId_menu(dto.getId_menu());
            menu.setNombre(dto.getNombre());
            menu.setDescripcion(dto.getDescripcion());
            menu.setPrecio(dto.getPrecio());
            menu.setRuta_imagen(dto.getRuta_imagen());
            menu.setDisponible(dto.getDisponible());
            menu.setOrden(dto.getOrden());
            menu.setTiempo_preparacion(dto.getTiempo_preparacion());
            menu.setDisponible_delivery(dto.getDisponible_delivery());
            menu.setDisponible_local(dto.getDisponible_local());
            menu.setEs_recomendado(dto.getEs_recomendado());
            menu.setEs_nuevo(dto.getEs_nuevo());
            menu.setEs_popular(dto.getEs_popular());
            menu.setRequiere_inventario(dto.getRequiere_inventario());
            menu.setStock(dto.getStock());
            Categoria categoria = categoriaService.obtenerPorId(dto.getCategoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
            menu.setCategoria(categoria);
            menu.setEn_promocion(dto.getEn_promocion());
            menu.setPrecio_promocion(dto.getPrecio_promocion());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (Boolean.TRUE.equals(dto.getEn_promocion())) {
                if (dto.getFecha_inicio_promocion() != null && !dto.getFecha_inicio_promocion().isBlank()) {
                    menu.setFecha_inicio_promocion(sdf.parse(dto.getFecha_inicio_promocion()));
                }
                if (dto.getFecha_fin_promocion() != null && !dto.getFecha_fin_promocion().isBlank()) {
                    menu.setFecha_fin_promocion(sdf.parse(dto.getFecha_fin_promocion()));
                }
            }
            menuService.actualizarMenu(menu);
            redirectAttributes.addFlashAttribute("mensaje", "Plato actualizado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/chifafu/admin/menu";
        }
        return "redirect:/chifafu/admin/menu";
    }

    // Eliminar plato
    @GetMapping("/eliminar/{id}")
    public String eliminarMenu(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            int resultado = menuService.eliminarMenu(id);
            if (resultado > 0) {
                redirectAttributes.addFlashAttribute("mensaje", "Plato eliminado exitosamente");
                redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            } else {
                redirectAttributes.addFlashAttribute("mensaje", "No se pudo eliminar el plato");
                redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar el plato: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }

        return "redirect:/chifafu/admin/menu";
    }

    // Activar plato
    @GetMapping("/activar/{id}")
    public String activarMenu(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            menuService.activarMenu(id);
            redirectAttributes.addFlashAttribute("mensaje", "Plato activado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al activar el plato: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }

        return "redirect:/chifafu/admin/menu";
    }

    // Desactivar plato
    @GetMapping("/desactivar/{id}")
    public String desactivarMenu(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            menuService.desactivarMenu(id);
            redirectAttributes.addFlashAttribute("mensaje", "Plato desactivado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al desactivar el plato: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }

        return "redirect:/chifafu/admin/menu";
    }

    // Marcar como recomendado
    @GetMapping("/recomendar/{id}")
    public String marcarComoRecomendado(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            menuService.marcarComoRecomendado(id);
            redirectAttributes.addFlashAttribute("mensaje", "Plato marcado como recomendado");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }

        return "redirect:/chifafu/admin/menu";
    }
}