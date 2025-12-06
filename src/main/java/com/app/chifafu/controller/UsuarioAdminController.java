package com.app.chifafu.controller;


import com.app.chifafu.dto.RegistroUsuarioDTO;
import com.app.chifafu.model.Usuario;
import com.app.chifafu.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/chifafu/admin/usuario")
public class UsuarioAdminController {

    private final UsuarioService usuarioService;

    public UsuarioAdminController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }


    //Mostrar la pagina con todos los usuarios
    @GetMapping("/lista")
    public String listarUsuarios(@RequestParam(required = false) String rol, Model model) {
        List<Usuario> usuarios;

        if (rol != null && !rol.isEmpty()) {
            usuarios = usuarioService.listarPorRol(rol);
        } else {
            usuarios = usuarioService.listarUsuarios();
        }

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("titulo", "Lista de Usuarios");
        model.addAttribute("rolSeleccionado", rol);
        return "administrador/usuario/listar";
    }

    //Mostrar el formulario para el registro de un administrador
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new RegistroUsuarioDTO());
        return "administrador/usuario/registro";
    }

    //Metodo para un nuevo administrador
    @PostMapping("/guardar")
    public String registrarAdministrador(
            @Valid @ModelAttribute("usuario") RegistroUsuarioDTO usuarioDTO,
            BindingResult result, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "administrador/usuario/registro";
        }

        if (!usuarioDTO.getPassword().equals(usuarioDTO.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.usuario", "Las contraseñas no coinciden");
            return "administrador/usuario/registro";
        }

        Usuario usuario = new Usuario();
        usuario.setNombres(usuarioDTO.getNombres());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setPassword(usuarioDTO.getPassword());
        usuario.setRol(Usuario.Rol.ADMINISTRADOR);
        usuario.setFecha_registro(new Date());

        usuarioService.crearUsuario(usuario);

        redirectAttributes.addFlashAttribute("mensaje", "Administrador creado correctamente");
        return "redirect:/chifafu/admin/usuario/registro";
    }

    //Eliminar un usuario
    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.eliminarUsuario(id);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar el usuario");
        }
        return "redirect:/chifafu/admin/usuario/lista";
    }

    //FALTA: Editar usuario



}
