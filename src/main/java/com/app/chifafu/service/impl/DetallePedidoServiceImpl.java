package com.app.chifafu.service.impl;

import com.app.chifafu.dao.DetallePedidoDAO;
import com.app.chifafu.model.DetallePedido;
import com.app.chifafu.service.DetallePedidoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetallePedidoServiceImpl implements DetallePedidoService {

    private final DetallePedidoDAO detallePedidoDAO;

    public DetallePedidoServiceImpl(DetallePedidoDAO detallePedidoDAO) {
        this.detallePedidoDAO = detallePedidoDAO;
    }

    @Override
    public DetallePedido crear(DetallePedido detallePedido) {
        detallePedido.calcularSubtotal();
        return detallePedidoDAO.crear(detallePedido);
    }

    @Override
    public Optional<DetallePedido> obtenerPorId(Long id) {
        return detallePedidoDAO.obtenerPorId(id);
    }

    @Override
    public List<DetallePedido> listarPorPedido(Long idPedido) {
        return detallePedidoDAO.listarPorPedido(idPedido);
    }

    @Override
    public List<DetallePedido> obtenerTodos() {
        return detallePedidoDAO.obtenerTodos();
    }

    @Override
    public DetallePedido actualizar(DetallePedido detallePedido) {
        detallePedido.calcularSubtotal();
        return detallePedidoDAO.actualizar(detallePedido);
    }

    @Override
    public boolean eliminar(Long id) {
        return detallePedidoDAO.eliminar(id);
    }

    @Override
    public boolean eliminarPorPedido(Long idPedido) {
        return detallePedidoDAO.eliminarPorPedido(idPedido);
    }

    @Override
    public Double calcularTotalPedido(Long idPedido) {
        List<DetallePedido> detalles = listarPorPedido(idPedido);
        return detalles.stream()
                .mapToDouble(DetallePedido::getSubtotal)
                .sum();
    }
}