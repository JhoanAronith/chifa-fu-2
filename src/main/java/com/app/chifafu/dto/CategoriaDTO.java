package com.app.chifafu.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaDTO {

    private Long id_categoria;
    private String nombre;
    private String descripcion;
    private Integer orden;
    private Boolean activa;

}
