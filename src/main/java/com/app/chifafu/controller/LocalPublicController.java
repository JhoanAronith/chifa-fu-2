package com.app.chifafu.controller;

import com.app.chifafu.service.LocalService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chifafu/public/locales")
public class LocalPublicController {


    private final LocalService localService;

    public LocalPublicController(LocalService localService) {
        this.localService = localService;
    }

    //Mostrar todos los locales al usuario
    @GetMapping
    public String listarLocales(Model model) {
        model.addAttribute("locales", localService.listarLocales());
        return "locales";
    }

}
