package com.app.chifafu.service;

import com.app.chifafu.model.Local;

import java.util.List;
import java.util.Optional;

public interface LocalService {

    void crearLocal(Local local);
    List<Local> listarLocales();
    Optional<Local> obtenerPorId(Long id);
    void actualizarLocal(Local local);
    int eliminarLocal(Long id);
    List<Local> listarActivos();
    List<Local> listarPorDelivery();
    List<Local> listarPorRecojo();
    Optional<Local> obtenerPorNombre(String nombre);
    void activarLocal(Long id);
    void desactivarLocal(Long id);
}