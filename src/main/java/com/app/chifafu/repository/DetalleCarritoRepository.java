package com.app.chifafu.repository;

import com.app.chifafu.dao.DetalleCarritoDAO;
import com.app.chifafu.model.Carrito;
import com.app.chifafu.model.DetalleCarrito;
import com.app.chifafu.model.Menu;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DetalleCarritoRepository implements DetalleCarritoDAO {

    private final JdbcTemplate jdbcTemplate;

    public DetalleCarritoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<DetalleCarrito> mapper = (rs, rowNum) -> {
        DetalleCarrito d = new DetalleCarrito();

        Carrito carrito = new Carrito();
        carrito.setId_carrito(rs.getLong("id_carrito"));

        Menu menu = new Menu();
        menu.setId_menu(rs.getLong("id_menu"));

        d.setId_detalle_carrito(rs.getLong("id_detalle_carrito"));
        d.setCarrito(carrito);
        d.setMenu(menu);
        d.setCantidad(rs.getInt("cantidad"));
        d.setPrecio_unitario(rs.getDouble("precio_unitario"));
        d.setFecha_agregado(rs.getTimestamp("fecha_agregado"));

        return d;
    };

    @Override
    public void crear(DetalleCarrito detalle) {
        String sql = """
            INSERT INTO detalle_carrito(id_carrito, id_menu, cantidad, precio_unitario, fecha_agregado)
            VALUES (?, ?, ?, ?, NOW())
        """;

        jdbcTemplate.update(sql,
                detalle.getCarrito().getId_carrito(),
                detalle.getMenu().getId_menu(),
                detalle.getCantidad(),
                detalle.getPrecio_unitario()
        );
    }

    @Override
    public List<DetalleCarrito> listarPorCarrito(Long idCarrito) {
        String sql = "SELECT dc.*, m.nombre, m.precio FROM detalle_carrito dc " +
                "JOIN menu m ON dc.id_menu = m.id_menu " +
                "WHERE dc.id_carrito = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            DetalleCarrito detalle = new DetalleCarrito();
            detalle.setId_detalle_carrito(rs.getLong("id_detalle_carrito"));
            detalle.setCantidad(rs.getInt("cantidad"));
            detalle.setPrecio_unitario(rs.getDouble("precio_unitario"));

            Menu menu = new Menu();
            menu.setId_menu(rs.getLong("id_menu"));
            menu.setNombre(rs.getString("nombre"));
            menu.setPrecio(rs.getDouble("precio"));
            detalle.setMenu(menu);

            return detalle;
        }, idCarrito);
    }

    @Override
    public Optional<DetalleCarrito> obtenerPorId(Long idDetalle) {
        String sql = "SELECT * FROM detalle_carrito WHERE id_detalle_carrito = ?";
        List<DetalleCarrito> lista = jdbcTemplate.query(sql, mapper, idDetalle);
        return lista.isEmpty() ? Optional.empty() : Optional.of(lista.get(0));
    }

    @Override
    public void actualizar(DetalleCarrito detalle) {
        String sql = """
            UPDATE detalle_carrito
            SET cantidad = ?, precio_unitario = ?
            WHERE id_detalle_carrito = ?
        """;

        jdbcTemplate.update(sql,
                detalle.getCantidad(),
                detalle.getPrecio_unitario(),
                detalle.getId_detalle_carrito()
        );
    }

    @Override
    public int eliminar(Long idDetalle) {
        String sql = "DELETE FROM detalle_carrito WHERE id_detalle_carrito = ?";
        return jdbcTemplate.update(sql, idDetalle);
    }

    @Override
    public int eliminarPorCarrito(Long idCarrito) {
        String sql = "DELETE FROM detalle_carrito WHERE id_carrito = ?";
        return jdbcTemplate.update(sql, idCarrito);
    }
}

