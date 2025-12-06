package com.app.chifafu.service.impl;

import com.app.chifafu.dao.ClienteDAO;
import com.app.chifafu.model.Cliente;
import com.app.chifafu.model.Usuario;
import com.app.chifafu.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {

    private final ClienteDAO clienteDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ClienteServiceImpl(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    @Override
    public void crearCliente(Cliente cliente) {
        Usuario usuario = cliente.getUsuario();
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        clienteDAO.crearCliente(cliente);
    }

    @Override
    public List<Cliente> listarClientes() {
        return clienteDAO.listarClientes();
    }

    @Override
    public Optional<Cliente> obtenerPorDni(Long dni) {
        return clienteDAO.obtenerPorDni(dni);
    }

    @Override
    public Optional<Cliente> obtenerPorId(Long id) {
        return clienteDAO.obtenerPorId(id);
    }

    @Override
    public Optional<Cliente> obtenerPorUsuarioId(Long idUsuario) {
        return clienteDAO.obtenerPorUsuarioId(idUsuario);
    }

    @Override
    public void actualizarCliente(Cliente cliente) {
        clienteDAO.actualizarCliente(cliente);
    }

    @Override
    public int eliminarCliente(Long id) {
        return clienteDAO.eliminarCliente(id);
    }

    @Override
    public Cliente buscarPorEmail(String email) {
        return clienteDAO.buscarPorEmail(email);
    }

}
