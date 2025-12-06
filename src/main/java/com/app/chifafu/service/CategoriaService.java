package com.app.chifafu.service;

import com.app.chifafu.model.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaService {

    void crearCategoria(Categoria categoria);

    List<Categoria> listarCategorias();

    Optional<Categoria> obtenerPorId(Long id);

    void actualizarCategoria(Categoria categoria);

    int eliminarCategoria(Long id);

    List<Categoria> listarActivas();

    List<Categoria> listarPorOrden();

    Optional<Categoria> obtenerPorNombre(String nombre);

    void activarCategoria(Long id);

    void desactivarCategoria(Long id);

}
