package com.app.chifafu.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        var authorities = authentication.getAuthorities();
        String redirectURL = request.getContextPath();

        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ADMINISTRADOR"))) {
            redirectURL += "/chifafu/admin/inicio";
        } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("CLIENTE"))) {
            redirectURL += "/chifafu/public/inicio";
        } else {
            redirectURL += "/login?error=unauthorized";
        }

        response.sendRedirect(redirectURL);

    }

}
