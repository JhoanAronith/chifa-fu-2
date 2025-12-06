package com.app.chifafu.controller;

import com.app.chifafu.model.Cliente;
import com.app.chifafu.model.Usuario;
import com.app.chifafu.service.ClienteService;
import com.app.chifafu.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/chifafu/cliente")
public class PerfilClienteController {

    private final UsuarioService usuarioService;
    private final ClienteService clienteService;

    public PerfilClienteController(UsuarioService usuarioService, ClienteService clienteService) {
        this.usuarioService = usuarioService;
        this.clienteService = clienteService;
    }

    //Pagina de perfil de cliente
    @GetMapping("/perfil")
    public String mostrarPerfil(Authentication authentication, Model model) {
        String email = authentication.getName();

        Optional<Usuario> usuarioOpt = usuarioService.obtenerPorEmail(email);

        if (usuarioOpt.isEmpty()) {
            return "redirect:/chifafu/public/login?error=notfound";
        }
        Usuario usuario = usuarioOpt.get();

        Optional<Cliente> clienteOpt = clienteService.obtenerPorUsuarioId(usuario.getId_usuario());
        if (clienteOpt.isEmpty()) {
            return "redirect:/chifafu/public/login?error=nocliente";
        }

        Cliente cliente = clienteOpt.get();

        model.addAttribute("usuario", usuario);
        model.addAttribute("cliente", cliente);
        return "cliente/perfil";
    }

    //Actualizar datos del perfil
    @PostMapping("/actualizar")
    public String actualizarPerfil(@ModelAttribute("cliente") Cliente cliente,
                                   RedirectAttributes redirectAttributes) {
        clienteService.actualizarCliente(cliente);
        redirectAttributes.addFlashAttribute("mensaje", "Perfil actualizado correctamente");
        return "redirect:/chifafu/cliente/perfil";
    }

}
