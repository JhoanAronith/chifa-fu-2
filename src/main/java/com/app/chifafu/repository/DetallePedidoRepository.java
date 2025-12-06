package com.app.chifafu.repository;

import com.app.chifafu.dao.DetallePedidoDAO;
import com.app.chifafu.model.DetallePedido;
import com.app.chifafu.model.Menu;
import com.app.chifafu.model.Pedido;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class DetallePedidoRepository implements DetallePedidoDAO {

    private final JdbcTemplate jdbcTemplate;
    private final PedidoRepository pedidoRepository;
    private final MenuRepository menuRepository;

    public DetallePedidoRepository(JdbcTemplate jdbcTemplate,
                                   PedidoRepository pedidoRepository,
                                   MenuRepository menuRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.pedidoRepository = pedidoRepository;
        this.menuRepository = menuRepository;
    }

    private RowMapper<DetallePedido> detallePedidoRowMapper = (rs, rowNum) -> mapearDetallePedido(rs);

    @Override
    public DetallePedido crear(DetallePedido detallePedido) {
        String sql = "INSERT INTO detalle_pedido (id_pedido, id_menu, cantidad, " +
                "precio_unitario, subtotal, fecha_agregado) " +
                "VALUES (?, ?, ?, ?, ?, NOW())";

        double subtotal = detallePedido.getPrecio_unitario() * detallePedido.getCantidad();

        jdbcTemplate.update(sql,
                detallePedido.getPedido().getId_pedido(),
                detallePedido.getMenu().getId_menu(),
                detallePedido.getCantidad(),
                detallePedido.getPrecio_unitario(),
                subtotal);

        String sqlId = "SELECT LAST_INSERT_ID()";
        Long idGenerado = jdbcTemplate.queryForObject(sqlId, Long.class);
        return obtenerPorId(idGenerado).orElse(null);
    }

    @Override
    public Optional<DetallePedido> obtenerPorId(Long id) {
        String sql = "SELECT * FROM detalle_pedido WHERE id_detalle_pedido = ?";
        List<DetallePedido> detalles = jdbcTemplate.query(sql, detallePedidoRowMapper, id);
        return detalles.stream().findFirst();
    }

    @Override
    public List<DetallePedido> listarPorPedido(Long idPedido) {
        String sql = "SELECT dp.*, m.nombre, m.id_menu " +
                "FROM detalle_pedido dp " +
                "JOIN menu m ON dp.id_menu = m.id_menu " +
                "WHERE dp.id_pedido = ? " +
                "ORDER BY dp.fecha_agregado DESC";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            DetallePedido detalle = new DetallePedido();
            detalle.setId_detalle_pedido(rs.getLong("id_detalle_pedido"));
            detalle.setCantidad(rs.getInt("cantidad"));
            detalle.setPrecio_unitario(rs.getDouble("precio_unitario"));
            detalle.setSubtotal(rs.getDouble("subtotal"));
            detalle.setFecha_agregado(rs.getTimestamp("fecha_agregado"));

            // Cargar menu
            Menu menu = new Menu();
            menu.setId_menu(rs.getLong("id_menu"));
            menu.setNombre(rs.getString("nombre"));
            detalle.setMenu(menu);

            // Cargar pedido
            Pedido pedido = new Pedido();
            pedido.setId_pedido(rs.getLong("id_pedido"));
            detalle.setPedido(pedido);

            return detalle;
        }, idPedido);
    }

    @Override
    public List<DetallePedido> obtenerTodos() {
        String sql = "SELECT * FROM detalle_pedido ORDER BY fecha_agregado DESC";
        return jdbcTemplate.query(sql, detallePedidoRowMapper);
    }

    @Override
    public DetallePedido actualizar(DetallePedido detallePedido) {
        String sql = "UPDATE detalle_pedido SET id_pedido=?, id_menu=?, cantidad=?, " +
                "precio_unitario=?, subtotal=? WHERE id_detalle_pedido=?";

        double subtotal = detallePedido.getPrecio_unitario() * detallePedido.getCantidad();

        jdbcTemplate.update(sql,
                detallePedido.getPedido().getId_pedido(),
                detallePedido.getMenu().getId_menu(),
                detallePedido.getCantidad(),
                detallePedido.getPrecio_unitario(),
                subtotal,
                detallePedido.getId_detalle_pedido());

        return obtenerPorId(detallePedido.getId_detalle_pedido()).orElse(null);
    }

    @Override
    public boolean eliminar(Long id) {
        String sql = "DELETE FROM detalle_pedido WHERE id_detalle_pedido = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    @Override
    public boolean eliminarPorPedido(Long idPedido) {
        String sql = "DELETE FROM detalle_pedido WHERE id_pedido = ?";
        return jdbcTemplate.update(sql, idPedido) > 0;
    }

    private DetallePedido mapearDetallePedido(ResultSet rs) throws SQLException {
        DetallePedido detalle = new DetallePedido();
        detalle.setId_detalle_pedido(rs.getLong("id_detalle_pedido"));
        detalle.setCantidad(rs.getInt("cantidad"));
        detalle.setPrecio_unitario(rs.getDouble("precio_unitario"));
        detalle.setSubtotal(rs.getDouble("subtotal"));
        detalle.setFecha_agregado(rs.getTimestamp("fecha_agregado"));

        // Cargar pedido
        Long idPedido = rs.getLong("id_pedido");
        if (idPedido != 0) {
            detalle.setPedido(pedidoRepository.obtenerPorId(idPedido).orElse(null));
        }

        // Cargar menu
        Long idMenu = rs.getLong("id_menu");
        if (idMenu != 0) {
            detalle.setMenu(menuRepository.obtenerPorId(idMenu).orElse(null));
        }

        return detalle;
    }
}