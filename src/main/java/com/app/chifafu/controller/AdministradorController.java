package com.app.chifafu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chifafu/admin")
public class AdministradorController {

    @GetMapping("/inicio")
    public String inicioAdministrador() {
        return "administrador/inicio";
    }

    @GetMapping("/pedidos")
    public String mostrarPedidos() {
        return "administrador/pedidos";
    }

    @GetMapping("/menus")
    public String mostrarMenus() {
        return "administrador/menus";
    }

    @GetMapping("/locales")
    public String mostrarLocales() {
        return "administrador/locales";
    }

}
