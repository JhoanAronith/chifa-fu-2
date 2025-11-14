package com.app.chifafu.service.impl;

import com.app.chifafu.dao.UsuarioDAO;
import com.app.chifafu.model.Usuario;
import com.app.chifafu.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioDAO usuarioDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    @Override
    public void crearUsuario(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioDAO.crearUsuario(usuario);
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioDAO.listarUsuarios();
    }

    @Override
    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioDAO.obtenerPorId(id);
    }

    @Override
    public void actualizarUsuario(Usuario usuario) {
        usuarioDAO.actualizarUsuario(usuario);
    }

    @Override
    public int eliminarUsuario(Long id) {
        return usuarioDAO.eliminarUsuario(id);
    }

    @Override
    public Optional<Usuario> obtenerPorEmail(String email) {
        return usuarioDAO.obtenerPorEmail(email);
    }

    @Override
    public List<Usuario> listarPorRol(String rol) {
        return usuarioDAO.listarPorRol(rol);
    }

}
