package com.app.chifafu.repository;

import com.app.chifafu.dao.CategoriaDAO;
import com.app.chifafu.model.Categoria;
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
public class CategoriaRepository implements CategoriaDAO {

    private final JdbcTemplate jdbcTemplate;

    public CategoriaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Categoria> categoriaRowMapper = (rs, rowNum) -> {
        Categoria categoria = new Categoria();
        categoria.setId_categoria(rs.getLong("id_categoria"));
        categoria.setNombre(rs.getString("nombre"));
        categoria.setDescripcion(rs.getString("descripcion"));
        categoria.setOrden(rs.getInt("orden"));
        categoria.setActiva(rs.getBoolean("activa"));
        categoria.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
        categoria.setFecha_actualizacion(rs.getTimestamp("fecha_actualizacion"));
        return categoria;
    };

    @Override
    public void crearCategoria(Categoria categoria) {
        String sql = "INSERT INTO categoria (nombre, descripcion, orden, activa, fecha_creacion, fecha_actualizacion) VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getDescripcion());
            ps.setInt(3, categoria.getOrden());
            ps.setBoolean(4, categoria.getActiva());
            ps.setTimestamp(5, new java.sql.Timestamp(new Date().getTime()));
            ps.setTimestamp(6, new java.sql.Timestamp(new Date().getTime()));
            return ps;
        }, keyHolder);

        categoria.setId_categoria(keyHolder.getKey().longValue());
    }

    @Override
    public List<Categoria> listarCategorias() {
        String sql = "SELECT * FROM categoria ORDER BY orden";
        return jdbcTemplate.query(sql, categoriaRowMapper);
    }

    @Override
    public Optional<Categoria> obtenerPorId(Long id) {
        String sql = "SELECT * FROM categoria WHERE id_categoria = ?";
        List<Categoria> categorias = jdbcTemplate.query(sql, categoriaRowMapper, id);
        return categorias.isEmpty() ? Optional.empty() : Optional.of(categorias.get(0));
    }

    @Override
    public void actualizarCategoria(Categoria categoria) {
        String sql = "UPDATE categoria SET nombre = ?, descripcion = ?, orden = ?, activa = ?, fecha_actualizacion = ? WHERE id_categoria = ?";
        jdbcTemplate.update(sql,
                categoria.getNombre(),
                categoria.getDescripcion(),
                categoria.getOrden(),
                categoria.getActiva(),
                new java.sql.Timestamp(new Date().getTime()),
                categoria.getId_categoria()
        );
    }

    @Override
    public int eliminarCategoria(Long id) {
        String sql = "DELETE FROM categoria WHERE id_categoria = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Categoria> listarActivas() {
        String sql = "SELECT * FROM categoria WHERE activa = true ORDER BY orden";
        return jdbcTemplate.query(sql, categoriaRowMapper);
    }

    @Override
    public List<Categoria> listarPorOrden() {
        String sql = "SELECT * FROM categoria ORDER BY orden ASC";
        return jdbcTemplate.query(sql, categoriaRowMapper);
    }

    @Override
    public Optional<Categoria> obtenerPorNombre(String nombre) {
        String sql = "SELECT * FROM categoria WHERE nombre = ?";
        List<Categoria> categorias = jdbcTemplate.query(sql, categoriaRowMapper, nombre);
        return categorias.isEmpty() ? Optional.empty() : Optional.of(categorias.get(0));
    }
}
