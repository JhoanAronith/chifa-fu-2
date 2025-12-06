package com.app.chifafu.dao;

import com.app.chifafu.model.Menu;

import java.util.List;
import java.util.Optional;

public interface MenuDAO {

    void crearMenu(Menu menu);
    List<Menu> listarMenus();
    Optional<Menu> obtenerPorId(Long id);
    void actualizarMenu(Menu menu);
    int eliminarMenu(Long id);
    List<Menu> listarDisponibles();
    List<Menu> listarPorCategoria(Long idCategoria);
    List<Menu> listarEnPromocion();
    List<Menu> listarRecomendados();
    List<Menu> listarNuevos();
    List<Menu> listarPopulares();
    List<Menu> listarMasVendidos(int limite);
    List<Menu> listarPorIngresos(int limite);
    List<Menu> buscarPorNombre(String nombre);
    void actualizarVentas(Long id, int cantidad, Double total);

}