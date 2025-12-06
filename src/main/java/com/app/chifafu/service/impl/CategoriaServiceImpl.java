package com.app.chifafu.service.impl;

import com.app.chifafu.dao.CategoriaDAO;
import com.app.chifafu.model.Categoria;
import com.app.chifafu.service.CategoriaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaDAO categoriaDAO;

    public CategoriaServiceImpl(CategoriaDAO categoriaDAO) {
        this.categoriaDAO = categoriaDAO;
    }

    @Override
    public void crearCategoria(Categoria categoria) {
        categoriaDAO.crearCategoria(categoria);
    }

    @Override
    public List<Categoria> listarCategorias() {
        return categoriaDAO.listarCategorias();
    }

    @Override
    public Optional<Categoria> obtenerPorId(Long id) {
        return categoriaDAO.obtenerPorId(id);
    }

    @Override
    public void actualizarCategoria(Categoria categoria) {
        categoriaDAO.actualizarCategoria(categoria);
    }

    @Override
    public int eliminarCategoria(Long id) {
        return categoriaDAO.eliminarCategoria(id);
    }

    @Override
    public List<Categoria> listarActivas() {
        return categoriaDAO.listarActivas();
    }

    @Override
    public List<Categoria> listarPorOrden() {
        return categoriaDAO.listarPorOrden();
    }

    @Override
    public Optional<Categoria> obtenerPorNombre(String nombre) {
        return categoriaDAO.obtenerPorNombre(nombre);
    }

    @Override
    public void activarCategoria(Long id) {
        Optional<Categoria> categoriaOpt = categoriaDAO.obtenerPorId(id);
        if (categoriaOpt.isPresent()) {
            Categoria categoria = categoriaOpt.get();
            categoria.setActiva(true);
            categoriaDAO.actualizarCategoria(categoria);
        }
    }

    @Override
    public void desactivarCategoria(Long id) {
        Optional<Categoria> categoriaOpt = categoriaDAO.obtenerPorId(id);
        if (categoriaOpt.isPresent()) {
            Categoria categoria = categoriaOpt.get();
            categoria.setActiva(false);
            categoriaDAO.actualizarCategoria(categoria);
        }
    }

}
