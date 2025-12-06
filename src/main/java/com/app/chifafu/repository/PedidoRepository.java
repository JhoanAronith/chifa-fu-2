package com.app.chifafu.repository;

import com.app.chifafu.dao.PedidoDAO;
import com.app.chifafu.model.Pedido;
import com.app.chifafu.model.Cliente;
import com.app.chifafu.model.Local;
import com.app.chifafu.model.Direccion;
import com.app.chifafu.model.Pago;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class PedidoRepository implements PedidoDAO {

    private final JdbcTemplate jdbcTemplate;
    private final ClienteRepository clienteRepository;
    private final LocalRepository localRepository;
    private final DireccionRepository direccionRepository;
    private final PagoRepository pagoRepository;

    public PedidoRepository(JdbcTemplate jdbcTemplate,
                            ClienteRepository clienteRepository,
                            LocalRepository localRepository,
                            DireccionRepository direccionRepository,
                            PagoRepository pagoRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.clienteRepository = clienteRepository;
        this.localRepository = localRepository;
        this.direccionRepository = direccionRepository;
        this.pagoRepository = pagoRepository;
    }

    private RowMapper<Pedido> pedidoRowMapper = (rs, rowNum) -> mapearPedido(rs);

    @Override
    public Pedido crear(Pedido pedido) {
        String sql = "INSERT INTO pedido (numero_pedido, id_cliente, id_local, id_direccion, " +
                "id_pago, estado, tipo_entrega, total, notas, fecha_pedido, fecha_actualizacion) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";

        jdbcTemplate.update(sql,
                pedido.getNumero_pedido(),
                pedido.getCliente().getId_cliente(),
                pedido.getLocal() != null ? pedido.getLocal().getId_local() : null,  // ← CORREGIDO
                pedido.getDireccion() != null ? pedido.getDireccion().getId_direccion() : null,
                pedido.getPago() != null ? pedido.getPago().getId_pago() : null,
                pedido.getEstado().toString(),
                pedido.getTipo_entrega().toString(),
                pedido.getTotal(),
                pedido.getNotas());

        return obtenerPorNumeroPedido(pedido.getNumero_pedido()).orElse(null);
    }

    @Override
    public Optional<Pedido> obtenerPorId(Long id) {
        String sql = "SELECT * FROM pedido WHERE id_pedido = ?";
        List<Pedido> pedidos = jdbcTemplate.query(sql, pedidoRowMapper, id);
        return pedidos.stream().findFirst();
    }

    @Override
    public Optional<Pedido> obtenerPorNumeroPedido(String numeroPedido) {
        String sql = "SELECT * FROM pedido WHERE numero_pedido = ?";
        List<Pedido> pedidos = jdbcTemplate.query(sql, pedidoRowMapper, numeroPedido);
        return pedidos.stream().findFirst();
    }

    @Override
    public List<Pedido> obtenerPorCliente(Long idCliente) {
        String sql = "SELECT * FROM pedido WHERE id_cliente = ? ORDER BY fecha_pedido DESC";
        return jdbcTemplate.query(sql, pedidoRowMapper, idCliente);
    }

    @Override
    public List<Pedido> obtenerPorEstado(Pedido.EstadoPedido estado) {
        String sql = "SELECT * FROM pedido WHERE estado = ? ORDER BY fecha_pedido DESC";
        return jdbcTemplate.query(sql, pedidoRowMapper, estado.toString());
    }

    @Override
    public List<Pedido> obtenerTodos() {
        String sql = "SELECT * FROM pedido ORDER BY fecha_pedido DESC";
        return jdbcTemplate.query(sql, pedidoRowMapper);
    }

    @Override
    public Pedido actualizar(Pedido pedido) {
        String sql = "UPDATE pedido SET numero_pedido=?, id_cliente=?, id_local=?, " +
                "id_direccion=?, id_pago=?, estado=?, tipo_entrega=?, total=?, " +
                "notas=?, fecha_actualizacion=NOW(), fecha_entrega=? WHERE id_pedido=?";

        jdbcTemplate.update(sql,
                pedido.getNumero_pedido(),
                pedido.getCliente().getId_cliente(),
                pedido.getLocal() != null ? pedido.getLocal().getId_local() : null,  // ← CORREGIDO
                pedido.getDireccion() != null ? pedido.getDireccion().getId_direccion() : null,
                pedido.getPago() != null ? pedido.getPago().getId_pago() : null,
                pedido.getEstado().toString(),
                pedido.getTipo_entrega().toString(),
                pedido.getTotal(),
                pedido.getNotas(),
                pedido.getFecha_entrega(),
                pedido.getId_pedido());

        return obtenerPorId(pedido.getId_pedido()).orElse(null);
    }

    @Override
    public boolean eliminar(Long id) {
        String sql = "DELETE FROM pedido WHERE id_pedido = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    private Pedido mapearPedido(ResultSet rs) throws SQLException {
        Pedido pedido = new Pedido();
        pedido.setId_pedido(rs.getLong("id_pedido"));
        pedido.setNumero_pedido(rs.getString("numero_pedido"));
        pedido.setEstado(Pedido.EstadoPedido.valueOf(rs.getString("estado")));
        pedido.setTipo_entrega(Pedido.TipoEntrega.valueOf(rs.getString("tipo_entrega")));
        pedido.setTotal(rs.getDouble("total"));
        pedido.setNotas(rs.getString("notas"));
        pedido.setFecha_pedido(rs.getTimestamp("fecha_pedido"));
        pedido.setFecha_actualizacion(rs.getTimestamp("fecha_actualizacion"));
        pedido.setFecha_entrega(rs.getTimestamp("fecha_entrega"));

        // Cargar relaciones
        Long idCliente = rs.getLong("id_cliente");
        if (idCliente != 0) {
            pedido.setCliente(clienteRepository.obtenerPorId(idCliente).orElse(null));
        }

        Long idLocal = rs.getLong("id_local");
        if (idLocal != 0) {
            pedido.setLocal(localRepository.obtenerPorId(idLocal).orElse(null));
        }

        Long idDireccion = rs.getLong("id_direccion");
        if (idDireccion != 0) {
            pedido.setDireccion(direccionRepository.obtenerPorId(idDireccion).orElse(null));
        }

        Long idPago = rs.getLong("id_pago");
        if (idPago != 0) {
            pedido.setPago(pagoRepository.obtenerPorId(idPago).orElse(null));
        }

        return pedido;
    }
}