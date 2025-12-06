package com.app.chifafu.service;

import com.app.chifafu.model.Pago;
import java.util.List;
import java.util.Optional;

public interface PagoService {
    Pago crear(Pago pago);
    Optional<Pago> obtenerPorId(Long id);
    Optional<Pago> obtenerPorNumeroTransaccion(String numeroTransaccion);
    List<Pago> obtenerPorEstado(Pago.EstadoPago estado);
    List<Pago> obtenerTodos();
    Pago actualizar(Pago pago);
    boolean eliminar(Long id);
    Pago cambiarEstado(Long idPago, Pago.EstadoPago nuevoEstado);
}