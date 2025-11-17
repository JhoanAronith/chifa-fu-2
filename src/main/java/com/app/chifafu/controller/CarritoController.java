package com.app.chifafu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chifafu/cliente")
public class CarritoController {

    @GetMapping("/carrito")
    public String mostrarCarrito() {
        return "cliente/carrito";
    }

}
