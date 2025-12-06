package com.app.chifafu.dao;

import com.app.chifafu.model.DetallePedido;
import java.util.List;
import java.util.Optional;

public interface DetallePedidoDAO {
    DetallePedido crear(DetallePedido detallePedido);
    Optional<DetallePedido> obtenerPorId(Long id);
    List<DetallePedido> listarPorPedido(Long idPedido);
    List<DetallePedido> obtenerTodos();
    DetallePedido actualizar(DetallePedido detallePedido);
    boolean eliminar(Long id);
    boolean eliminarPorPedido(Long idPedido);
}
