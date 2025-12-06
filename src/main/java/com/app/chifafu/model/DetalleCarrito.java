package com.app.chifafu.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "detalle_carrito")
public class DetalleCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_detalle_carrito;

    @ManyToOne
    @JoinColumn(name = "id_carrito", nullable = false)
    private Carrito carrito;

    @ManyToOne
    @JoinColumn(name = "id_menu", nullable = false)
    private Menu menu;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Double precio_unitario;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date fecha_agregado;

    public DetalleCarrito() {
        this.cantidad = 1;
    }

    @PrePersist
    protected void onCreate() {
        fecha_agregado = new Date();
    }

    public Double getSubtotal() {
        return precio_unitario * cantidad;
    }
}