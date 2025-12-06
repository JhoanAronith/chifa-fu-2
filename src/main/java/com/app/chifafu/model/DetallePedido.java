package com.app.chifafu.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "detalle_pedido")
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_detalle_pedido;

    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "id_menu", nullable = false)
    private Menu menu;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Double precio_unitario;

    @Column(nullable = false)
    private Double subtotal;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date fecha_agregado;

    public DetallePedido() {
        this.cantidad = 1;
    }

    @PrePersist
    protected void onCreate() {
        fecha_agregado = new Date();
        calcularSubtotal();
    }

    @PreUpdate
    protected void onUpdate() {
        calcularSubtotal();
    }

    public void calcularSubtotal() {
        this.subtotal = precio_unitario * cantidad;
    }
}