package com.app.chifafu.service;

import com.app.chifafu.model.DetalleCarrito;

import java.util.List;
import java.util.Optional;

public interface DetalleCarritoService {

    void crear(DetalleCarrito detalle);
    List<DetalleCarrito> listarPorCarrito(Long idCarrito);
    Optional<DetalleCarrito> obtenerPorId(Long idDetalle);
    void actualizar(DetalleCarrito detalle);
    int eliminar(Long idDetalle);
    int eliminarPorCarrito(Long idCarrito);

}
