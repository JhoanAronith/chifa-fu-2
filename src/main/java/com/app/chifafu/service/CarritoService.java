package com.app.chifafu.service;

import com.app.chifafu.model.Carrito;

import java.util.Optional;

public interface CarritoService {

    Carrito obtenerOCrear(Long idCliente);
    Optional<Carrito> obtenerPorCliente(Long idCliente);
    Optional<Carrito> obtenerPorId(Long idCarrito);
    void actualizar(Carrito carrito);
    int eliminar(Long idCarrito);

}
