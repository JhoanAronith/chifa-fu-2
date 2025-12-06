package com.app.chifafu.dao;

import com.app.chifafu.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteDAO {

    void crearCliente(Cliente cliente);

    List<Cliente> listarClientes();

    Optional<Cliente> obtenerPorDni(Long dni);

    Optional<Cliente> obtenerPorId(Long id);

    Optional<Cliente> obtenerPorUsuarioId(Long idUsuario);

    void actualizarCliente(Cliente cliente);

    int eliminarCliente(Long id);

    Cliente buscarPorEmail(String email);
}
