package com.app.chifafu.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "local")
public class Local {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_local;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 300)
    private String direccion;

    @Column(length = 100)
    private String distrito;

    @Column(length = 15)
    private String telefono;

    @Column(length = 200)
    private String horario;

    private Integer capacidad;

    private String ruta_imagen;

    @Column(nullable = false)
    private Boolean activo;

    @Column(nullable = false)
    private Boolean acepta_delivery;

    @Column(nullable = false)
    private Boolean acepta_recojo;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date fecha_creacion;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha_actualizacion;

    public Local() {
        this.activo = true;
        this.acepta_delivery = true;
        this.acepta_recojo = true;
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

    public String getDireccionCompleta() {
        if (distrito != null && !distrito.isEmpty()) {
            return direccion + ", " + distrito;
        }
        return direccion;
    }
}