package com.app.chifafu.service;

import com.app.chifafu.model.DetallePedido;
import java.util.List;
import java.util.Optional;

public interface DetallePedidoService {
    DetallePedido crear(DetallePedido detallePedido);
    Optional<DetallePedido> obtenerPorId(Long id);
    List<DetallePedido> listarPorPedido(Long idPedido);
    List<DetallePedido> obtenerTodos();
    DetallePedido actualizar(DetallePedido detallePedido);
    boolean eliminar(Long id);
    boolean eliminarPorPedido(Long idPedido);
    Double calcularTotalPedido(Long idPedido);
}