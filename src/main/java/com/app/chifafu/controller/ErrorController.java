package com.app.chifafu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chifafu/error")
public class ErrorController {

    @GetMapping("/403")
    public String error403() {
        return "error/403";
    }
}
