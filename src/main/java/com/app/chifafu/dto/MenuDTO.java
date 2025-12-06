package com.app.chifafu.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuDTO {

    private Long id_menu;
    private String nombre;
    private String descripcion;
    private Double precio;
    private String ruta_imagen;
    private Boolean disponible;
    private Long categoriaId;
    private Integer orden;
    private Boolean en_promocion;
    private Double precio_promocion;
    private String fecha_inicio_promocion;
    private String fecha_fin_promocion;
    private Boolean es_recomendado;
    private Boolean es_nuevo;
    private Boolean es_popular;
    private Integer tiempo_preparacion;
    private Boolean disponible_delivery;
    private Boolean disponible_local;
    private Integer stock;
    private Boolean requiere_inventario;
    private Integer cantidad_vendida = 0;
    private Double total_ingresos = 0.0;

}
