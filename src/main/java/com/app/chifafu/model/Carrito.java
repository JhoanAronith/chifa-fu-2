package com.app.chifafu.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "carrito")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_carrito;

    @OneToOne
    @JoinColumn(name = "id_cliente", nullable = false, unique = true)
    private Cliente cliente;

    @Column(nullable = false)
    private Boolean activo;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date fecha_creacion;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha_actualizacion;

    public Carrito() {
        this.activo = true;
    }

    @PrePersist
    protected void onCreate() {
        fecha_creacion = new Date();
        fecha_actualizacion = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        fecha_actualizacion = new Date();
    }
}