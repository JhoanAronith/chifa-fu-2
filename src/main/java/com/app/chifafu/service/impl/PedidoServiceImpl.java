package com.app.chifafu.service.impl;

import com.app.chifafu.dao.PedidoDAO;
import com.app.chifafu.model.Pedido;
import com.app.chifafu.service.PedidoService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoDAO pedidoDAO;

    public PedidoServiceImpl(PedidoDAO pedidoDAO) {
        this.pedidoDAO = pedidoDAO;
    }

    @Override
    public Pedido crear(Pedido pedido) {
        if (pedido.getNumero_pedido() == null || pedido.getNumero_pedido().isEmpty()) {
            pedido.setNumero_pedido(generarNumeroPedido());
        }
        return pedidoDAO.crear(pedido);
    }

    @Override
    public Optional<Pedido> obtenerPorId(Long id) {
        return pedidoDAO.obtenerPorId(id);
    }

    @Override
    public Optional<Pedido> obtenerPorNumeroPedido(String numeroPedido) {
        return pedidoDAO.obtenerPorNumeroPedido(numeroPedido);
    }

    @Override
    public List<Pedido> obtenerPorCliente(Long idCliente) {
        return pedidoDAO.obtenerPorCliente(idCliente);
    }

    @Override
    public List<Pedido> obtenerPorEstado(Pedido.EstadoPedido estado) {
        return pedidoDAO.obtenerPorEstado(estado);
    }

    @Override
    public List<Pedido> obtenerTodos() {
        return pedidoDAO.obtenerTodos();
    }

    @Override
    public Pedido actualizar(Pedido pedido) {
        return pedidoDAO.actualizar(pedido);
    }

    @Override
    public boolean eliminar(Long id) {
        return pedidoDAO.eliminar(id);
    }

    @Override
    public String generarNumeroPedido() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        return "PED-" + timestamp;
    }

    @Override
    public Pedido cambiarEstado(Long idPedido, Pedido.EstadoPedido nuevoEstado) {
        Optional<Pedido> pedidoOpt = obtenerPorId(idPedido);
        if (pedidoOpt.isPresent()) {
            Pedido pedido = pedidoOpt.get();
            pedido.setEstado(nuevoEstado);
            return actualizar(pedido);
        }
        return null;
    }
}