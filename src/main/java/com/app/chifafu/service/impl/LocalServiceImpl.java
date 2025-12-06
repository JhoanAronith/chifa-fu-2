package com.app.chifafu.service.impl;

import com.app.chifafu.dao.LocalDAO;
import com.app.chifafu.model.Local;
import com.app.chifafu.service.LocalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LocalServiceImpl implements LocalService {
    private final LocalDAO localDAO;
    public LocalServiceImpl(LocalDAO localDAO) {
        this.localDAO = localDAO;
    }
    @Override
    public void crearLocal(Local local) {
        localDAO.crearLocal(local);
    }
    @Override
    public List<Local> listarLocales() {
        return localDAO.listarLocales();
    }
    @Override
    public Optional<Local> obtenerPorId(Long id) {
        return localDAO.obtenerPorId(id);
    }
    @Override
    public void actualizarLocal(Local local) {
        localDAO.actualizarLocal(local);
    }
    @Override
    public int eliminarLocal(Long id) {
        return localDAO.eliminarLocal(id);
    }
    @Override
    public List<Local> listarActivos() {
        return localDAO.listarActivos();
    }
    @Override
    public List<Local> listarPorDelivery() {
        return localDAO.listarPorDelivery();
    }
    @Override
    public List<Local> listarPorRecojo() {
        return localDAO.listarPorRecojo();
    }
    @Override
    public Optional<Local> obtenerPorNombre(String nombre) {
        return localDAO.obtenerPorNombre(nombre);
    }
    @Override
    public void activarLocal(Long id) {
        Optional<Local> localOpt = localDAO.obtenerPorId(id);
        if (localOpt.isPresent()) {
            Local local = localOpt.get();
            local.setActivo(true);
            localDAO.actualizarLocal(local);
        }
    }
    @Override
    public void desactivarLocal(Long id) {
        Optional<Local> localOpt = localDAO.obtenerPorId(id);
        if (localOpt.isPresent()) {
            Local local = localOpt.get();
            local.setActivo(false);
            localDAO.actualizarLocal(local);
        }
    }
}