package com.app.chifafu.controller;

import com.app.chifafu.model.Categoria;
import com.app.chifafu.model.Menu;
import com.app.chifafu.service.CategoriaService;
import com.app.chifafu.service.MenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/chifafu/public")
public class MenuPublicController {

    private final MenuService menuService;
    private final CategoriaService categoriaService;

    public MenuPublicController(MenuService menuService, CategoriaService categoriaService) {
        this.menuService = menuService;
        this.categoriaService = categoriaService;
    }

    // Mostrar menú completo
    @GetMapping("/menu")
    public String mostrarMenu(Model model, @RequestParam(required = false) Long categoria) {
        List<Categoria> categorias = categoriaService.listarActivas();
        List<Menu> menus;

        if (categoria != null) {
            menus = menuService.listarPorCategoria(categoria);
            model.addAttribute("categoriaSeleccionada", categoria);
        } else {
            menus = menuService.listarDisponibles();
        }

        model.addAttribute("categorias", categorias);
        model.addAttribute("menus", menus);
        return "menu";
    }

    // Ver detalle de un plato
    @GetMapping("/menu/detalle/{id}")
    public String verDetalleMenu(@PathVariable Long id, Model model) {
        Optional<Menu> menuOpt = menuService.obtenerPorId(id);

        if (menuOpt.isEmpty()) {
            return "redirect:/chifafu/public/menu";
        }

        model.addAttribute("menu", menuOpt.get());
        return "menuDetalle";
    }

    // Mostrar platos recomendados
    @GetMapping("/menu/recomendados")
    public String mostrarRecomendados(Model model) {
        List<Menu> menusRecomendados = menuService.listarRecomendados();
        model.addAttribute("menus", menusRecomendados);
        model.addAttribute("titulo", "Platos Recomendados");
        return "menu";
    }

    // Mostrar platos en promoción
    @GetMapping("/menu/promociones")
    public String mostrarPromociones(Model model) {
        List<Menu> menusPromocion = menuService.listarEnPromocion();
        model.addAttribute("menus", menusPromocion);
        model.addAttribute("titulo", "Promociones");
        return "menu";
    }
}