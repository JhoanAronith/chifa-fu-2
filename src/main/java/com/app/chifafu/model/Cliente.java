package com.app.chifafu.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_cliente;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    private String direccion;
    @Column(unique = true)
    private String telefono;
    @Column(unique = true)
    private String dni;

}
