package com.app.chifafu.service.impl;

import com.app.chifafu.dao.DireccionDAO;
import com.app.chifafu.model.Direccion;
import com.app.chifafu.service.DireccionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DireccionServiceImpl implements DireccionService {

    private final DireccionDAO direccionDAO;

    public DireccionServiceImpl(DireccionDAO direccionDAO) {
        this.direccionDAO = direccionDAO;
    }

    @Override
    public Direccion crear(Direccion direccion) {
        return direccionDAO.crear(direccion);
    }

    @Override
    public Optional<Direccion> obtenerPorId(Long id) {
        return direccionDAO.obtenerPorId(id);
    }

    @Override
    public List<Direccion> obtenerTodas() {
        return direccionDAO.obtenerTodas();
    }

    @Override
    public Direccion actualizar(Direccion direccion) {
        return direccionDAO.actualizar(direccion);
    }

    @Override
    public boolean eliminar(Long id) {
        return direccionDAO.eliminar(id);
    }
}