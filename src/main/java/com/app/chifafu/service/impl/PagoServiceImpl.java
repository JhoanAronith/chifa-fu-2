package com.app.chifafu.service.impl;

import com.app.chifafu.dao.PagoDAO;
import com.app.chifafu.model.Pago;
import com.app.chifafu.service.PagoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PagoServiceImpl implements PagoService {

    private final PagoDAO pagoDAO;

    public PagoServiceImpl(PagoDAO pagoDAO) {
        this.pagoDAO = pagoDAO;
    }

    @Override
    public Pago crear(Pago pago) {
        return pagoDAO.crear(pago);
    }

    @Override
    public Optional<Pago> obtenerPorId(Long id) {
        return pagoDAO.obtenerPorId(id);
    }

    @Override
    public Optional<Pago> obtenerPorNumeroTransaccion(String numeroTransaccion) {
        return pagoDAO.obtenerPorNumeroTransaccion(numeroTransaccion);
    }

    @Override
    public List<Pago> obtenerPorEstado(Pago.EstadoPago estado) {
        return pagoDAO.obtenerPorEstado(estado);
    }

    @Override
    public List<Pago> obtenerTodos() {
        return pagoDAO.obtenerTodos();
    }

    @Override
    public Pago actualizar(Pago pago) {
        return pagoDAO.actualizar(pago);
    }

    @Override
    public boolean eliminar(Long id) {
        return pagoDAO.eliminar(id);
    }

    @Override
    public Pago cambiarEstado(Long idPago, Pago.EstadoPago nuevoEstado) {
        Optional<Pago> pagoOpt = obtenerPorId(idPago);
        if (pagoOpt.isPresent()) {
            Pago pago = pagoOpt.get();
            pago.setEstado_pago(nuevoEstado);
            return actualizar(pago);
        }
        return null;
    }
}