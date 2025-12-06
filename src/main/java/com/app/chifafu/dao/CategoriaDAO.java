package com.app.chifafu.dao;

import com.app.chifafu.model.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaDAO {

    void crearCategoria(Categoria categoria);
    List<Categoria> listarCategorias();
    Optional<Categoria> obtenerPorId(Long id);
    void actualizarCategoria(Categoria categoria);
    int eliminarCategoria(Long id);
    List<Categoria> listarActivas();
    List<Categoria> listarPorOrden();
    Optional<Categoria> obtenerPorNombre(String nombre);

}
