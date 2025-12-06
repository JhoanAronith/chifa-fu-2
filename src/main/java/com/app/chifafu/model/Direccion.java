package com.app.chifafu.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "direccion")
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_direccion;

    @Column(nullable = false, length = 200)
    private String calle;

    @Column(length = 20)
    private String numero;

    @Column(nullable = false, length = 100)
    private String distrito;

    @Column(length = 300)
    private String referencia;

    @Column(length = 15)
    private String telefono_contacto;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date fecha_creacion;

    public Direccion() {
    }

    @PrePersist
    protected void onCreate() {
        fecha_creacion = new Date();
    }

    public String getDireccionCompleta() {
        StringBuilder sb = new StringBuilder();
        sb.append(calle);
        if (numero != null && !numero.isEmpty()) {
            sb.append(" ").append(numero);
        }
        sb.append(", ").append(distrito);
        if (referencia != null && !referencia.isEmpty()) {
            sb.append(" (").append(referencia).append(")");
        }
        return sb.toString();
    }
}