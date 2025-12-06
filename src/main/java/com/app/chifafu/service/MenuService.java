package com.app.chifafu.service;

import com.app.chifafu.model.Menu;

import java.util.List;
import java.util.Optional;

public interface MenuService {

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

    void activarMenu(Long id);

    void desactivarMenu(Long id);

    void marcarComoRecomendado(Long id);

    void desmarcarComoRecomendado(Long id);

    void marcarComoNuevo(Long id);

    void desmarcarComoNuevo(Long id);

    void marcarComoPopular(Long id);

    void desmarcarComoPopular(Long id);

}