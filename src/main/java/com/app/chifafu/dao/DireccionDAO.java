package com.app.chifafu.dao;

import com.app.chifafu.model.Direccion;
import java.util.List;
import java.util.Optional;

public interface DireccionDAO {
    Direccion crear(Direccion direccion);
    Optional<Direccion> obtenerPorId(Long id);
    List<Direccion> obtenerTodas();
    Direccion actualizar(Direccion direccion);
    boolean eliminar(Long id);
}