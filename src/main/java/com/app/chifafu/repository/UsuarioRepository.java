package com.app.chifafu.repository;

import com.app.chifafu.dao.UsuarioDAO;
import com.app.chifafu.model.Cliente;
import com.app.chifafu.model.Usuario;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class UsuarioRepository implements UsuarioDAO {

    private final JdbcTemplate jdbcTemplate;

    public UsuarioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Usuario> usuarioRowMapper = (rs, rowNum) -> {
        Usuario usuario = new Usuario();
        usuario.setId_usuario(rs.getLong("id_usuario"));
        usuario.setNombres(rs.getString("nombres"));
        usuario.setEmail(rs.getString("email"));
        usuario.setPassword(rs.getString("password"));
        usuario.setRol(Usuario.Rol.valueOf(rs.getString("rol")));
        usuario.setFecha_registro(rs.getTimestamp("fecha_registro"));
        return usuario;
    };


    @Override
    public void crearUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuario (nombres, email, password, rol, fecha_registro) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, usuario.getNombres());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getPassword());
            ps.setString(4, usuario.getRol().name());
            ps.setTimestamp(5, new java.sql.Timestamp(new Date().getTime()));
            return ps;
        }, keyHolder);

        usuario.setId_usuario(keyHolder.getKey().longValue());
    }

    @Override
    public List<Usuario> listarUsuarios() {
        String sql = "SELECT * FROM usuario";
        return jdbcTemplate.query(sql, usuarioRowMapper);
    }

    @Override
    public Optional<Usuario> obtenerPorId(Long id) {
        String sql = "SELECT * FROM usuario WHERE id_usuario = ?";
        List<Usuario> usuarios = jdbcTemplate.query(sql, usuarioRowMapper, id);
        return usuarios.stream().findFirst();
    }

    @Override
    public void actualizarUsuario(Usuario usuario) {
        String sql = "UPDATE usuario SET nombres = ?, email = ?, password = ?, rol = ? WHERE id_usuario = ?";
        jdbcTemplate.update(sql,
                usuario.getNombres(),
                usuario.getEmail(),
                usuario.getPassword(),
                usuario.getRol().name(),
                usuario.getId_usuario());
    }

    @Override
    public int eliminarUsuario(Long id) {
        String sqlCliente = "DELETE FROM cliente WHERE id_usuario = ?";
        jdbcTemplate.update(sqlCliente, id);

        String sqlUsuario = "DELETE FROM usuario WHERE id_usuario = ?";
        return jdbcTemplate.update(sqlUsuario, id);
    }

    @Override
    public Optional<Usuario> obtenerPorEmail(String email) {
        String sql = "SELECT u.*, c.id_cliente, c.direccion, c.telefono, c.dni " +
                "FROM usuario u " +
                "LEFT JOIN cliente c ON u.id_usuario = c.id_usuario " +
                "WHERE u.email = ?";

        List<Usuario> usuarios = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Usuario usuario = new Usuario();
            usuario.setId_usuario(rs.getLong("id_usuario"));
            usuario.setNombres(rs.getString("nombres"));
            usuario.setEmail(rs.getString("email"));
            usuario.setPassword(rs.getString("password"));
            usuario.setRol(Usuario.Rol.valueOf(rs.getString("rol")));
            usuario.setFecha_registro(rs.getTimestamp("fecha_registro"));

            // Cargar cliente si existe
            Long idCliente = rs.getLong("id_cliente");
            if (idCliente != 0) {
                Cliente cliente = new Cliente();
                cliente.setId_cliente(idCliente);
                cliente.setDireccion(rs.getString("direccion"));
                cliente.setTelefono(rs.getString("telefono"));
                cliente.setDni(rs.getString("dni"));
                cliente.setUsuario(usuario);
                usuario.setCliente(cliente);
            }

            return usuario;
        }, email);

        return usuarios.stream().findFirst();
    }

    @Override
    public List<Usuario> listarPorRol(String rol) {
        String sql = "SELECT * FROM usuario WHERE rol = ?";
        return jdbcTemplate.query(sql, usuarioRowMapper, rol);
    }

}

