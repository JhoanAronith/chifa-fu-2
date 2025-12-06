package com.app.chifafu.service;

import com.app.chifafu.model.Pedido;
import java.util.List;
import java.util.Optional;

public interface PedidoService {
    Pedido crear(Pedido pedido);
    Optional<Pedido> obtenerPorId(Long id);
    Optional<Pedido> obtenerPorNumeroPedido(String numeroPedido);
    List<Pedido> obtenerPorCliente(Long idCliente);
    List<Pedido> obtenerPorEstado(Pedido.EstadoPedido estado);
    List<Pedido> obtenerTodos();
    Pedido actualizar(Pedido pedido);
    boolean eliminar(Long id);
    String generarNumeroPedido();
    Pedido cambiarEstado(Long idPedido, Pedido.EstadoPedido nuevoEstado);
}
