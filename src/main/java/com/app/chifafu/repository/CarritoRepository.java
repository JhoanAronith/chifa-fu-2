package com.app.chifafu.repository;

import com.app.chifafu.dao.CarritoDAO;
import com.app.chifafu.model.Carrito;
import com.app.chifafu.model.Cliente;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class CarritoRepository implements CarritoDAO {

    private final JdbcTemplate jdbcTemplate;

    public CarritoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Carrito> mapper = (rs, rowNum) -> {
        Carrito c = new Carrito();
        Cliente cliente = new Cliente();
        cliente.setId_cliente(rs.getLong("id_cliente"));

        c.setId_carrito(rs.getLong("id_carrito"));
        c.setCliente(cliente);
        c.setActivo(rs.getBoolean("activo"));
        c.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
        c.setFecha_actualizacion(rs.getTimestamp("fecha_actualizacion"));
        return c;
    };

    @Override
    public void crear(Carrito carrito) {
        String sql = """
            INSERT INTO carrito(id_cliente, activo, fecha_creacion, fecha_actualizacion)
            VALUES (?, ?, NOW(), NOW())
        """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, carrito.getCliente().getId_cliente());
            ps.setBoolean(2, carrito.getActivo());
            return ps;
        }, keyHolder);

        carrito.setId_carrito(keyHolder.getKey().longValue());
    }

    @Override
    public Optional<Carrito> obtenerPorId(Long idCarrito) {
        String sql = "SELECT * FROM carrito WHERE id_carrito = ?";
        List<Carrito> lista = jdbcTemplate.query(sql, mapper, idCarrito);
        return lista.isEmpty() ? Optional.empty() : Optional.of(lista.get(0));
    }

    @Override
    public Optional<Carrito> obtenerPorCliente(Long idCliente) {
        String sql = "SELECT * FROM carrito WHERE id_cliente = ?";
        List<Carrito> lista = jdbcTemplate.query(sql, mapper, idCliente);
        return lista.isEmpty() ? Optional.empty() : Optional.of(lista.get(0));
    }

    @Override
    public void actualizar(Carrito carrito) {
        String sql = """
            UPDATE carrito
            SET activo = ?, fecha_actualizacion = NOW()
            WHERE id_carrito = ?
        """;

        jdbcTemplate.update(sql,
                carrito.getActivo(),
                carrito.getId_carrito()
        );
    }

    @Override
    public int eliminar(Long idCarrito) {
        String sql = "DELETE FROM carrito WHERE id_carrito = ?";
        return jdbcTemplate.update(sql, idCarrito);
    }
}

