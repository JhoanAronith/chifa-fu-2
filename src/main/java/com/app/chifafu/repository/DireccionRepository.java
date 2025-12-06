package com.app.chifafu.repository;

import com.app.chifafu.dao.DireccionDAO;
import com.app.chifafu.model.Direccion;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class DireccionRepository implements DireccionDAO {

    private final JdbcTemplate jdbcTemplate;

    public DireccionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<Direccion> direccionRowMapper = (rs, rowNum) -> mapearDireccion(rs);

    @Override
    public Direccion crear(Direccion direccion) {
        String sql = "INSERT INTO direccion (calle, numero, distrito, referencia, " +
                "telefono_contacto, fecha_creacion) " +
                "VALUES (?, ?, ?, ?, ?, NOW())";

        jdbcTemplate.update(sql,
                direccion.getCalle(),
                direccion.getNumero(),
                direccion.getDistrito(),
                direccion.getReferencia(),
                direccion.getTelefono_contacto());

        String sqlId = "SELECT LAST_INSERT_ID()";
        Long idGenerado = jdbcTemplate.queryForObject(sqlId, Long.class);
        return obtenerPorId(idGenerado).orElse(null);
    }

    @Override
    public Optional<Direccion> obtenerPorId(Long id) {
        String sql = "SELECT * FROM direccion WHERE id_direccion = ?";
        List<Direccion> direcciones = jdbcTemplate.query(sql, direccionRowMapper, id);
        return direcciones.stream().findFirst();
    }

    @Override
    public List<Direccion> obtenerTodas() {
        String sql = "SELECT * FROM direccion ORDER BY fecha_creacion DESC";
        return jdbcTemplate.query(sql, direccionRowMapper);
    }

    @Override
    public Direccion actualizar(Direccion direccion) {
        String sql = "UPDATE direccion SET calle=?, numero=?, distrito=?, " +
                "referencia=?, telefono_contacto=? WHERE id_direccion=?";

        jdbcTemplate.update(sql,
                direccion.getCalle(),
                direccion.getNumero(),
                direccion.getDistrito(),
                direccion.getReferencia(),
                direccion.getTelefono_contacto(),
                direccion.getId_direccion());

        return obtenerPorId(direccion.getId_direccion()).orElse(null);
    }

    @Override
    public boolean eliminar(Long id) {
        String sql = "DELETE FROM direccion WHERE id_direccion = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    private Direccion mapearDireccion(ResultSet rs) throws SQLException {
        Direccion direccion = new Direccion();
        direccion.setId_direccion(rs.getLong("id_direccion"));
        direccion.setCalle(rs.getString("calle"));
        direccion.setNumero(rs.getString("numero"));
        direccion.setDistrito(rs.getString("distrito"));
        direccion.setReferencia(rs.getString("referencia"));
        direccion.setTelefono_contacto(rs.getString("telefono_contacto"));
        direccion.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
        return direccion;
    }
}