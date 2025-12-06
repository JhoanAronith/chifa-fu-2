package com.app.chifafu.service.impl;

import com.app.chifafu.dao.MenuDAO;
import com.app.chifafu.model.Menu;
import com.app.chifafu.service.MenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MenuServiceImpl implements MenuService {

    private final MenuDAO menuDAO;

    public MenuServiceImpl(MenuDAO menuDAO) {
        this.menuDAO = menuDAO;
    }

    @Override
    public void crearMenu(Menu menu) {
        menuDAO.crearMenu(menu);
    }

    @Override
    public List<Menu> listarMenus() {
        return menuDAO.listarMenus();
    }

    @Override
    public Optional<Menu> obtenerPorId(Long id) {
        return menuDAO.obtenerPorId(id);
    }

    @Override
    public void actualizarMenu(Menu menu) {
        menuDAO.actualizarMenu(menu);
    }

    @Override
    public int eliminarMenu(Long id) {
        return menuDAO.eliminarMenu(id);
    }

    @Override
    public List<Menu> listarDisponibles() {
        return menuDAO.listarDisponibles();
    }

    @Override
    public List<Menu> listarPorCategoria(Long idCategoria) {
        return menuDAO.listarPorCategoria(idCategoria);
    }

    @Override
    public List<Menu> listarEnPromocion() {
        return menuDAO.listarEnPromocion();
    }

    @Override
    public List<Menu> listarRecomendados() {
        return menuDAO.listarRecomendados();
    }

    @Override
    public List<Menu> listarNuevos() {
        return menuDAO.listarNuevos();
    }

    @Override
    public List<Menu> listarPopulares() {
        return menuDAO.listarPopulares();
    }

    @Override
    public List<Menu> listarMasVendidos(int limite) {
        return menuDAO.listarMasVendidos(limite);
    }

    @Override
    public List<Menu> listarPorIngresos(int limite) {
        return menuDAO.listarPorIngresos(limite);
    }

    @Override
    public List<Menu> buscarPorNombre(String nombre) {
        return menuDAO.buscarPorNombre(nombre);
    }

    @Override
    public void actualizarVentas(Long id, int cantidad, Double total) {
        menuDAO.actualizarVentas(id, cantidad, total);
    }

    @Override
    public void activarMenu(Long id) {
        Optional<Menu> menuOpt = menuDAO.obtenerPorId(id);
        if (menuOpt.isPresent()) {
            Menu menu = menuOpt.get();
            menu.setDisponible(true);
            menuDAO.actualizarMenu(menu);
        }
    }

    @Override
    public void desactivarMenu(Long id) {
        Optional<Menu> menuOpt = menuDAO.obtenerPorId(id);
        if (menuOpt.isPresent()) {
            Menu menu = menuOpt.get();
            menu.setDisponible(false);
            menuDAO.actualizarMenu(menu);
        }
    }

    @Override
    public void marcarComoRecomendado(Long id) {
        Optional<Menu> menuOpt = menuDAO.obtenerPorId(id);
        if (menuOpt.isPresent()) {
            Menu menu = menuOpt.get();
            menu.setEs_recomendado(true);
            menuDAO.actualizarMenu(menu);
        }
    }

    @Override
    public void desmarcarComoRecomendado(Long id) {
        Optional<Menu> menuOpt = menuDAO.obtenerPorId(id);
        if (menuOpt.isPresent()) {
            Menu menu = menuOpt.get();
            menu.setEs_recomendado(false);
            menuDAO.actualizarMenu(menu);
        }
    }

    @Override
    public void marcarComoNuevo(Long id) {
        Optional<Menu> menuOpt = menuDAO.obtenerPorId(id);
        if (menuOpt.isPresent()) {
            Menu menu = menuOpt.get();
            menu.setEs_nuevo(true);
            menuDAO.actualizarMenu(menu);
        }
    }

    @Override
    public void desmarcarComoNuevo(Long id) {
        Optional<Menu> menuOpt = menuDAO.obtenerPorId(id);
        if (menuOpt.isPresent()) {
            Menu menu = menuOpt.get();
            menu.setEs_nuevo(false);
            menuDAO.actualizarMenu(menu);
        }
    }

    @Override
    public void marcarComoPopular(Long id) {
        Optional<Menu> menuOpt = menuDAO.obtenerPorId(id);
        if (menuOpt.isPresent()) {
            Menu menu = menuOpt.get();
            menu.setEs_popular(true);
            menuDAO.actualizarMenu(menu);
        }
    }

    @Override
    public void desmarcarComoPopular(Long id) {
        Optional<Menu> menuOpt = menuDAO.obtenerPorId(id);
        if (menuOpt.isPresent()) {
            Menu menu = menuOpt.get();
            menu.setEs_popular(false);
            menuDAO.actualizarMenu(menu);
        }
    }
}