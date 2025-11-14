package com.app.chifafu.model;

import jakarta.persistence.*;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_usuario;
    private String nombres;
    @Column(unique = true)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Rol rol;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha_registro;

    public Usuario() {
    }

    public enum Rol {
        ADMINISTRADOR,
        CLIENTE
    }

}
