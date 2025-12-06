package com.app.chifafu.service.impl;

import com.app.chifafu.dao.CarritoDAO;
import com.app.chifafu.model.Carrito;
import com.app.chifafu.model.Cliente;
import com.app.chifafu.service.CarritoService;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CarritoServiceImpl implements CarritoService {

    private final CarritoDAO carritoDAO;

    public CarritoServiceImpl(CarritoDAO carritoDAO) {
        this.carritoDAO = carritoDAO;
    }

    @Override
    public Carrito obtenerOCrear(Long idCliente) {
        return carritoDAO.obtenerPorCliente(idCliente)
                .orElseGet(() -> {
                    Carrito c = new Carrito();
                    Cliente cliente = new Cliente();
                    cliente.setId_cliente(idCliente);

                    c.setCliente(cliente);
                    c.setActivo(true);

                    carritoDAO.crear(c);
                    return c;
                });
    }

    @Override
    public Optional<Carrito> obtenerPorCliente(Long idCliente) {
        return carritoDAO.obtenerPorCliente(idCliente);
    }

    @Override
    public Optional<Carrito> obtenerPorId(Long idCarrito) {
        return carritoDAO.obtenerPorId(idCarrito);
    }

    @Override
    public void actualizar(Carrito carrito) {
        carritoDAO.actualizar(carrito);
    }

    @Override
    public int eliminar(Long idCarrito) {
        return carritoDAO.eliminar(idCarrito);
    }
}
