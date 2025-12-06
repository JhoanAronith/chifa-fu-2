package com.app.chifafu.dao;

import com.app.chifafu.model.Carrito;

import java.util.Optional;

public interface CarritoDAO {

    void crear(Carrito carrito);
    Optional<Carrito> obtenerPorId(Long idCarrito);
    Optional<Carrito> obtenerPorCliente(Long idCliente);
    void actualizar(Carrito carrito);
    int eliminar(Long idCarrito);

}
