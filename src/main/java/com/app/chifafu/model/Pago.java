package com.app.chifafu.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "pago")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_pago;

    @Column(nullable = false)
    private Double monto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetodoPago metodo_pago;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPago estado_pago;

    @Column(length = 100)
    private String numero_transaccion;

    @Column(length = 100)
    private String comprobante;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date fecha_pago;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha_actualizacion;

    public Pago() {
        this.estado_pago = EstadoPago.PENDIENTE;
    }

    @PrePersist
    protected void onCreate() {
        fecha_pago = new Date();
        fecha_actualizacion = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        fecha_actualizacion = new Date();
    }

    public enum MetodoPago {
        EFECTIVO,
        TARJETA,
        YAPE,
        PLIN,
        TRANSFERENCIA
    }

    public enum EstadoPago {
        PENDIENTE,
        PROCESANDO,
        COMPLETADO,
        FALLIDO,
        REEMBOLSADO
    }

    public boolean isPagado() {
        return estado_pago == EstadoPago.COMPLETADO;
    }

    public boolean isPendiente() {
        return estado_pago == EstadoPago.PENDIENTE;
    }
}