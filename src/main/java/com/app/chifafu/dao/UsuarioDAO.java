package com.app.chifafu.dao;

import com.app.chifafu.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioDAO {

    void crearUsuario(Usuario usuario);
    List<Usuario> listarUsuarios();
    Optional<Usuario> obtenerPorId(Long id);
    void actualizarUsuario(Usuario usuario);
    int eliminarUsuario(Long id);
    Optional<Usuario> obtenerPorEmail(String email);
    List<Usuario> listarPorRol(String rol);

}
