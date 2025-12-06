package com.app.chifafu.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_menu;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false)
    private Double precio;

    private String ruta_imagen;

    @Column(nullable = false)
    private Boolean disponible;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    private Integer orden;

    @Column(nullable = false)
    private Integer cantidad_vendida;

    @Column(nullable = false)
    private Double total_ingresos;

    private Boolean en_promocion;

    private Double precio_promocion;

    @Temporal(TemporalType.DATE)
    private Date fecha_inicio_promocion;

    @Temporal(TemporalType.DATE)
    private Date fecha_fin_promocion;

    private Boolean es_recomendado;

    private Boolean es_nuevo;

    private Boolean es_popular;

    private Integer tiempo_preparacion;

    private Boolean disponible_delivery;

    private Boolean disponible_local;

    private Integer stock;

    private Boolean requiere_inventario;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date fecha_creacion;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha_actualizacion;

    public Menu() {
        this.disponible = true;
        this.cantidad_vendida = 0;
        this.total_ingresos = 0.0;
        this.orden = 0;
        this.en_promocion = false;
        this.es_recomendado = false;
        this.es_nuevo = false;
        this.es_popular = false;
        this.disponible_delivery = true;
        this.disponible_local = true;
        this.requiere_inventario = false;
        this.stock = 0;
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

    public boolean promocionActiva() {
        if (!en_promocion || fecha_inicio_promocion == null || fecha_fin_promocion == null) {
            return false;
        }
        Date hoy = new Date();
        return !hoy.before(fecha_inicio_promocion) && !hoy.after(fecha_fin_promocion);
    }

    public Double getPrecioFinal() {
        if (promocionActiva() && precio_promocion != null && precio_promocion > 0) {
            return precio_promocion;
        }
        return precio != null ? precio : 0.0;
    }
}