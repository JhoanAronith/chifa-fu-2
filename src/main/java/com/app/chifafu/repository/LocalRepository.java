package com.app.chifafu.repository;

import com.app.chifafu.dao.LocalDAO;
import com.app.chifafu.model.Local;
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
public class LocalRepository implements LocalDAO {
    private final JdbcTemplate jdbcTemplate;
    public LocalRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    private final RowMapper<Local> localRowMapper = (rs, rowNum) -> {
        Local local = new Local();
        local.setId_local(rs.getLong("id_local"));
        local.setNombre(rs.getString("nombre"));
        local.setDireccion(rs.getString("direccion"));
        local.setDistrito(rs.getString("distrito"));
        local.setTelefono(rs.getString("telefono"));
        local.setHorario(rs.getString("horario"));
        local.setCapacidad(rs.getInt("capacidad"));
        local.setRuta_imagen(rs.getString("ruta_imagen"));
        local.setActivo(rs.getBoolean("activo"));
        local.setAcepta_delivery(rs.getBoolean("acepta_delivery"));
        local.setAcepta_recojo(rs.getBoolean("acepta_recojo"));
        local.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
        local.setFecha_actualizacion(rs.getTimestamp("fecha_actualizacion"));
        return local;
    };
    @Override
    public void crearLocal(Local local) {
        String sql = "INSERT INTO local (nombre, direccion, distrito, telefono, horario, capacidad, ruta_imagen, activo, acepta_delivery, acepta_recojo, fecha_creacion, fecha_actualizacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, local.getNombre());
            ps.setString(2, local.getDireccion());
            ps.setString(3, local.getDistrito());
            ps.setString(4, local.getTelefono());
            ps.setString(5, local.getHorario());
            ps.setInt(6, local.getCapacidad());
            ps.setString(7, local.getRuta_imagen());
            ps.setBoolean(8, local.getActivo());
            ps.setBoolean(9, local.getAcepta_delivery());
            ps.setBoolean(10, local.getAcepta_recojo());
            ps.setTimestamp(11, new java.sql.Timestamp(new Date().getTime()));
            ps.setTimestamp(12, new java.sql.Timestamp(new Date().getTime()));
            return ps;
        }, keyHolder);

        local.setId_local(keyHolder.getKey().longValue());
    }
    @Override
    public List<Local> listarLocales() {
        String sql = "SELECT * FROM local ORDER BY nombre";
        return jdbcTemplate.query(sql, localRowMapper);
    }
    @Override
    public Optional<Local> obtenerPorId(Long id) {
        String sql = "SELECT * FROM local WHERE id_local = ?";
        List<Local> locales = jdbcTemplate.query(sql, localRowMapper, id);
        return locales.isEmpty() ? Optional.empty() : Optional.of(locales.get(0));
    }
    @Override
    public void actualizarLocal(Local local) {
        String sql = "UPDATE local SET nombre = ?, direccion = ?, distrito = ?, telefono = ?, horario = ?, capacidad = ?, ruta_imagen = ?, activo = ?, acepta_delivery = ?, acepta_recojo = ?, fecha_actualizacion = ? WHERE id_local = ?";
        jdbcTemplate.update(sql,
                local.getNombre(),
                local.getDireccion(),
                local.getDistrito(),
                local.getTelefono(),
                local.getHorario(),
                local.getCapacidad(),
                local.getRuta_imagen(),
                local.getActivo(),
                local.getAcepta_delivery(),
                local.getAcepta_recojo(),
                new java.sql.Timestamp(new Date().getTime()),
                local.getId_local()
        );
    }
    @Override
    public int eliminarLocal(Long id) {
        String sql = "DELETE FROM local WHERE id_local = ?";
        return jdbcTemplate.update(sql, id);
    }
    @Override
    public List<Local> listarActivos() {
        String sql = "SELECT * FROM local WHERE activo = true ORDER BY nombre";
        return jdbcTemplate.query(sql, localRowMapper);
    }
    @Override
    public List<Local> listarPorDelivery() {
        String sql = "SELECT * FROM local WHERE activo = true AND acepta_delivery = true ORDER BY nombre";
        return jdbcTemplate.query(sql, localRowMapper);
    }
    @Override
    public List<Local> listarPorRecojo() {
        String sql = "SELECT * FROM local WHERE activo = true AND acepta_recojo = true ORDER BY nombre";
        return jdbcTemplate.query(sql, localRowMapper);
    }
    @Override
    public Optional<Local> obtenerPorNombre(String nombre) {
        String sql = "SELECT * FROM local WHERE nombre = ?";
        List<Local> locales = jdbcTemplate.query(sql, localRowMapper, nombre);
        return locales.isEmpty() ? Optional.empty() : Optional.of(locales.get(0));
    }
}