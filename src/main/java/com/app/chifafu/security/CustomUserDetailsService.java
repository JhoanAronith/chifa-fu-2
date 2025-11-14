package com.app.chifafu.security;

import com.app.chifafu.dao.UsuarioDAO;
import com.app.chifafu.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioDAO usuarioDAO;

    @Autowired
    public CustomUserDetailsService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioDAO.obtenerPorEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario con encontrado con email: " + email));
        return new CustomUserDetails(usuario);
    }

}
