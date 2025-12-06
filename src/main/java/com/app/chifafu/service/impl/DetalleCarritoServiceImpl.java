package com.app.chifafu.service.impl;

import com.app.chifafu.dao.DetalleCarritoDAO;
import com.app.chifafu.model.DetalleCarrito;
import com.app.chifafu.service.DetalleCarritoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DetalleCarritoServiceImpl implements DetalleCarritoService {

    private final DetalleCarritoDAO detalleDAO;

    public DetalleCarritoServiceImpl(DetalleCarritoDAO detalleDAO) {
        this.detalleDAO = detalleDAO;
    }

    @Override
    public void crear(DetalleCarrito detalle) {
        detalleDAO.crear(detalle);
    }

    @Override
    public List<DetalleCarrito> listarPorCarrito(Long idCarrito) {
        return detalleDAO.listarPorCarrito(idCarrito);
    }

    @Override
    public Optional<DetalleCarrito> obtenerPorId(Long idDetalle) {
        return detalleDAO.obtenerPorId(idDetalle);
    }

    @Override
    public void actualizar(DetalleCarrito detalle) {
        detalleDAO.actualizar(detalle);
    }

    @Override
    public int eliminar(Long idDetalle) {
        return detalleDAO.eliminar(idDetalle);
    }

    @Override
    public int eliminarPorCarrito(Long idCarrito) {
        return detalleDAO.eliminarPorCarrito(idCarrito);
    }
}
