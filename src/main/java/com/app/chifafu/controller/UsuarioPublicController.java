package com.app.chifafu.controller;

import com.app.chifafu.dto.RegistroClienteDTO;
import com.app.chifafu.model.Cliente;
import com.app.chifafu.model.Usuario;
import com.app.chifafu.service.ClienteService;
import com.app.chifafu.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;

@Controller
@RequestMapping("/chifafu/public")
public class UsuarioPublicController {

    private final UsuarioService usuarioService;
    private final ClienteService clienteService;

    public UsuarioPublicController(UsuarioService usuarioService, ClienteService clienteService) {
        this.usuarioService = usuarioService;
        this.clienteService = clienteService;
    }

    //Mostrar Página de Inicio
    @GetMapping("/inicio")
    public String paginaInicio(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            var authorities = authentication.getAuthorities();
            if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ADMINISTRADOR"))) {
                return "redirect:/chifafu/admin/inicio";
            }
        }
        return "inicio";
    }

    //Mostrar pagina de Login
    @GetMapping("/login")
    public String login(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            var authorities = authentication.getAuthorities();
            if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ADMINISTRADOR"))) {
                return "redirect:/chifafu/admin/inicio";
            } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("CLIENTE"))) {
                return "redirect:/chifafu/cliente/inicio";
            }
        }
        return "login";
    }

    //Mostrar página de registro
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            var authorities = authentication.getAuthorities();
            if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ADMINISTRADOR"))) {
                return "redirect:/chifafu/admin/inicio";
            } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("CLIENTE"))) {
                return "redirect:/chifafu/cliente/inicio";
            }
        }
        model.addAttribute("cliente", new RegistroClienteDTO());
        return "registro";
    }

    //Guardar un nuevo cliente y su usuario vinculado
    @PostMapping("/guardar")
    public String registrarCliente(
            @Valid @ModelAttribute("cliente") RegistroClienteDTO clienteDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            return "registro";
        }

        if (!clienteDTO.getPassword().equals(clienteDTO.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.cliente", "Las contraseñas no coinciden");
            return "registro";
        }

        Usuario usuario = new Usuario();
        usuario.setNombres(clienteDTO.getNombres());
        usuario.setEmail(clienteDTO.getEmail());
        usuario.setPassword(clienteDTO.getPassword());
        usuario.setRol(Usuario.Rol.CLIENTE);
        usuario.setFecha_registro(new Date());

        usuarioService.crearUsuario(usuario);

        Cliente cliente = new Cliente();
        cliente.setUsuario(usuario);
        cliente.setDireccion(clienteDTO.getDireccion());
        cliente.setTelefono(clienteDTO.getTelefono());
        cliente.setDni(clienteDTO.getDni());

        clienteService.crearCliente(cliente);

        redirectAttributes.addFlashAttribute("mensaje", "Cliente registrado correctamente");
        return "redirect:/chifafu/public/login";
    }

}
