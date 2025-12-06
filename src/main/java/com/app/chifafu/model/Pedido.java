package com.app.chifafu.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_pedido;

    @Column(nullable = false, unique = true, length = 20)
    private String numero_pedido;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_local", nullable = true)
    private Local local;

    @ManyToOne
    @JoinColumn(name = "id_direccion")
    private Direccion direccion;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_pago")
    private Pago pago;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEntrega tipo_entrega;

    @Column(nullable = false)
    private Double total;

    @Column(length = 500)
    private String notas;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date fecha_pedido;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha_actualizacion;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha_entrega;

    public Pedido() {
        this.estado = EstadoPedido.PENDIENTE;
        this.total = 0.0;
    }

    @PrePersist
    protected void onCreate() {
        fecha_pedido = new Date();
        fecha_actualizacion = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        fecha_actualizacion = new Date();
    }

    public enum EstadoPedido {
        PENDIENTE,
        CONFIRMADO,
        EN_PREPARACION,
        LISTO,
        EN_CAMINO,
        ENTREGADO,
        CANCELADO
    }

    public enum TipoEntrega {
        DELIVERY,
        RECOJO_LOCAL
    }

    public boolean isDelivery() {
        return tipo_entrega == TipoEntrega.DELIVERY;
    }

    public boolean isRecojoLocal() {
        return tipo_entrega == TipoEntrega.RECOJO_LOCAL;
    }

    public boolean isActivo() {
        return estado != EstadoPedido.ENTREGADO && estado != EstadoPedido.CANCELADO;
    }

    public boolean puedeSerCancelado() {
        return estado == EstadoPedido.PENDIENTE || estado == EstadoPedido.CONFIRMADO;
    }
}