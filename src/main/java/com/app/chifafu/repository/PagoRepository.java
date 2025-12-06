package com.app.chifafu.repository;

import com.app.chifafu.dao.PagoDAO;
import com.app.chifafu.model.Pago;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class PagoRepository implements PagoDAO {

    private final JdbcTemplate jdbcTemplate;

    public PagoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<Pago> pagoRowMapper = (rs, rowNum) -> mapearPago(rs);

    @Override
    public Pago crear(Pago pago) {
        String sql = "INSERT INTO pago (monto, metodo_pago, estado_pago, numero_transaccion, " +
                "comprobante, fecha_pago, fecha_actualizacion) " +
                "VALUES (?, ?, ?, ?, ?, NOW(), NOW())";

        jdbcTemplate.update(sql,
                pago.getMonto(),
                pago.getMetodo_pago().toString(),
                pago.getEstado_pago().toString(),
                pago.getNumero_transaccion(),
                pago.getComprobante());

        // Obtener el ID generado
        String sqlId = "SELECT LAST_INSERT_ID()";
        Long idGenerado = jdbcTemplate.queryForObject(sqlId, Long.class);
        return obtenerPorId(idGenerado).orElse(null);
    }

    @Override
    public Optional<Pago> obtenerPorId(Long id) {
        String sql = "SELECT * FROM pago WHERE id_pago = ?";
        List<Pago> pagos = jdbcTemplate.query(sql, pagoRowMapper, id);
        return pagos.stream().findFirst();
    }

    @Override
    public Optional<Pago> obtenerPorNumeroTransaccion(String numeroTransaccion) {
        String sql = "SELECT * FROM pago WHERE numero_transaccion = ?";
        List<Pago> pagos = jdbcTemplate.query(sql, pagoRowMapper, numeroTransaccion);
        return pagos.stream().findFirst();
    }

    @Override
    public List<Pago> obtenerPorEstado(Pago.EstadoPago estado) {
        String sql = "SELECT * FROM pago WHERE estado_pago = ? ORDER BY fecha_pago DESC";
        return jdbcTemplate.query(sql, pagoRowMapper, estado.toString());
    }

    @Override
    public List<Pago> obtenerTodos() {
        String sql = "SELECT * FROM pago ORDER BY fecha_pago DESC";
        return jdbcTemplate.query(sql, pagoRowMapper);
    }

    @Override
    public Pago actualizar(Pago pago) {
        String sql = "UPDATE pago SET monto=?, metodo_pago=?, estado_pago=?, " +
                "numero_transaccion=?, comprobante=?, fecha_actualizacion=NOW() WHERE id_pago=?";

        jdbcTemplate.update(sql,
                pago.getMonto(),
                pago.getMetodo_pago().toString(),
                pago.getEstado_pago().toString(),
                pago.getNumero_transaccion(),
                pago.getComprobante(),
                pago.getId_pago());

        return obtenerPorId(pago.getId_pago()).orElse(null);
    }

    @Override
    public boolean eliminar(Long id) {
        String sql = "DELETE FROM pago WHERE id_pago = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    private Pago mapearPago(ResultSet rs) throws SQLException {
        Pago pago = new Pago();
        pago.setId_pago(rs.getLong("id_pago"));
        pago.setMonto(rs.getDouble("monto"));
        pago.setMetodo_pago(Pago.MetodoPago.valueOf(rs.getString("metodo_pago")));
        pago.setEstado_pago(Pago.EstadoPago.valueOf(rs.getString("estado_pago")));
        pago.setNumero_transaccion(rs.getString("numero_transaccion"));
        pago.setComprobante(rs.getString("comprobante"));
        pago.setFecha_pago(rs.getTimestamp("fecha_pago"));
        pago.setFecha_actualizacion(rs.getTimestamp("fecha_actualizacion"));
        return pago;
    }
}