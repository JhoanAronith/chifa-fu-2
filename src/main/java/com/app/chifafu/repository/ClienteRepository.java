package com.app.chifafu.repository;

import com.app.chifafu.dao.ClienteDAO;
import com.app.chifafu.model.Cliente;
import com.app.chifafu.model.Usuario;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ClienteRepository implements ClienteDAO {

    private final JdbcTemplate jdbcTemplate;

    public ClienteRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Cliente> clienteRowMapper = (rs, rowNum) -> {
        Cliente cliente = new Cliente();
        cliente.setId_cliente(rs.getLong("id_cliente"));
        cliente.setDireccion(rs.getString("direccion"));
        cliente.setTelefono(rs.getString("telefono"));
        cliente.setDni(rs.getString("dni"));

        Usuario usuario = new Usuario();
        usuario.setId_usuario(rs.getLong("id_usuario"));
        cliente.setUsuario(usuario);
        return cliente;
    };


    @Override
    public void crearCliente(Cliente cliente) {
        String sql = "INSERT INTO cliente (id_usuario, direccion, telefono, dni) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                cliente.getUsuario().getId_usuario(),
                cliente.getDireccion(),
                cliente.getTelefono(),
                cliente.getDni()
        );
    }

    @Override
    public List<Cliente> listarClientes() {
        String sql = "SELECT * FROM cliente";
        return jdbcTemplate.query(sql, clienteRowMapper);
    }

    @Override
    public Optional<Cliente> obtenerPorDni(Long dni) {
        String sql = "SELECT * FROM cliente WHERE dni = ?)";
        List<Cliente> clientes = jdbcTemplate.query(sql, clienteRowMapper, dni);
        return clientes.stream().findFirst();
    }

    @Override
    public Optional<Cliente> obtenerPorId(Long id) {
        String sql = "SELECT * FROM cliente WHERE id_cliente = ?";
        List<Cliente> clientes = jdbcTemplate.query(sql, clienteRowMapper, id);
        return clientes.stream().findFirst();
    }

    @Override
    public Optional<Cliente> obtenerPorUsuarioId(Long idUsuario) {
        String sql = "SELECT * FROM cliente WHERE id_usuario = ?";
        List<Cliente> clientes = jdbcTemplate.query(sql, clienteRowMapper, idUsuario);
        return clientes.stream().findFirst();
    }

    @Override
    public void actualizarCliente(Cliente cliente) {
        String sql = "UPDATE cliente SET direccion = ?, telefono = ? WHERE id_cliente = ?";
        jdbcTemplate.update(sql,
                cliente.getDireccion(),
                cliente.getTelefono(),
                cliente.getId_cliente()
        );
    }

    @Override
    public int eliminarCliente(Long id) {
        String sql = "DELETE FROM cliente WHERE id_cliente = ?";
        return jdbcTemplate.update(sql, id);
    }
}
