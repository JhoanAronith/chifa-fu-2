package com.app.chifafu.repository;

import com.app.chifafu.dao.MenuDAO;
import com.app.chifafu.model.Categoria;
import com.app.chifafu.model.Menu;
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
public class MenuRepository implements MenuDAO {

    private final JdbcTemplate jdbcTemplate;

    public MenuRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Menu> menuRowMapper = (rs, rowNum) -> {
        Menu menu = new Menu();
        menu.setId_menu(rs.getLong("id_menu"));
        menu.setNombre(rs.getString("nombre"));
        menu.setDescripcion(rs.getString("descripcion"));
        menu.setPrecio(rs.getDouble("precio"));
        menu.setRuta_imagen(rs.getString("ruta_imagen"));
        menu.setDisponible(rs.getBoolean("disponible"));
        menu.setOrden(rs.getInt("orden"));
        menu.setCantidad_vendida(rs.getInt("cantidad_vendida"));
        menu.setTotal_ingresos(rs.getDouble("total_ingresos"));
        menu.setEn_promocion(rs.getBoolean("en_promocion"));

        // Manejo de precio_promocion que puede ser NULL
        Double precioPromocion = rs.getObject("precio_promocion", Double.class);
        menu.setPrecio_promocion(precioPromocion);

        menu.setFecha_inicio_promocion(rs.getDate("fecha_inicio_promocion"));
        menu.setFecha_fin_promocion(rs.getDate("fecha_fin_promocion"));
        menu.setEs_recomendado(rs.getBoolean("es_recomendado"));
        menu.setEs_nuevo(rs.getBoolean("es_nuevo"));
        menu.setEs_popular(rs.getBoolean("es_popular"));
        menu.setTiempo_preparacion(rs.getInt("tiempo_preparacion"));
        menu.setDisponible_delivery(rs.getBoolean("disponible_delivery"));
        menu.setDisponible_local(rs.getBoolean("disponible_local"));
        menu.setStock(rs.getInt("stock"));
        menu.setRequiere_inventario(rs.getBoolean("requiere_inventario"));
        menu.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
        menu.setFecha_actualizacion(rs.getTimestamp("fecha_actualizacion"));

        // Cargar la categoría
        Categoria categoria = new Categoria();
        categoria.setId_categoria(rs.getLong("id_categoria"));
        menu.setCategoria(categoria);

        return menu;
    };

    @Override
    public void crearMenu(Menu menu) {
        String sql = "INSERT INTO menu (nombre, descripcion, precio, ruta_imagen, disponible, id_categoria, orden, " +
                "cantidad_vendida, total_ingresos, en_promocion, precio_promocion, fecha_inicio_promocion, " +
                "fecha_fin_promocion, es_recomendado, es_nuevo, es_popular, tiempo_preparacion, disponible_delivery, " +
                "disponible_local, stock, requiere_inventario, fecha_creacion, fecha_actualizacion) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, menu.getNombre());
            ps.setString(2, menu.getDescripcion());
            ps.setDouble(3, menu.getPrecio());
            ps.setString(4, menu.getRuta_imagen());
            ps.setBoolean(5, menu.getDisponible());
            ps.setLong(6, menu.getCategoria().getId_categoria());
            ps.setInt(7, menu.getOrden());
            ps.setInt(8, menu.getCantidad_vendida());
            ps.setDouble(9, menu.getTotal_ingresos());
            ps.setBoolean(10, menu.getEn_promocion());
            ps.setObject(11, menu.getPrecio_promocion());
            ps.setDate(12, menu.getFecha_inicio_promocion() != null ? new java.sql.Date(menu.getFecha_inicio_promocion().getTime()) : null);
            ps.setDate(13, menu.getFecha_fin_promocion() != null ? new java.sql.Date(menu.getFecha_fin_promocion().getTime()) : null);
            ps.setBoolean(14, menu.getEs_recomendado());
            ps.setBoolean(15, menu.getEs_nuevo());
            ps.setBoolean(16, menu.getEs_popular());
            ps.setInt(17, menu.getTiempo_preparacion());
            ps.setBoolean(18, menu.getDisponible_delivery());
            ps.setBoolean(19, menu.getDisponible_local());
            ps.setInt(20, menu.getStock());
            ps.setBoolean(21, menu.getRequiere_inventario());
            ps.setTimestamp(22, new java.sql.Timestamp(new Date().getTime()));
            ps.setTimestamp(23, new java.sql.Timestamp(new Date().getTime()));
            return ps;
        }, keyHolder);

        menu.setId_menu(keyHolder.getKey().longValue());
    }

    @Override
    public List<Menu> listarMenus() {
        String sql = "SELECT * FROM menu ORDER BY orden, nombre";
        return jdbcTemplate.query(sql, menuRowMapper);
    }

    @Override
    public Optional<Menu> obtenerPorId(Long id) {
        String sql = "SELECT * FROM menu WHERE id_menu = ?";
        List<Menu> menus = jdbcTemplate.query(sql, menuRowMapper, id);
        return menus.isEmpty() ? Optional.empty() : Optional.of(menus.get(0));
    }

    @Override
    public void actualizarMenu(Menu menu) {
        String sql = "UPDATE menu SET nombre = ?, descripcion = ?, precio = ?, ruta_imagen = ?, disponible = ?, " +
                "id_categoria = ?, orden = ?, cantidad_vendida = ?, total_ingresos = ?, en_promocion = ?, " +
                "precio_promocion = ?, fecha_inicio_promocion = ?, fecha_fin_promocion = ?, es_recomendado = ?, " +
                "es_nuevo = ?, es_popular = ?, tiempo_preparacion = ?, disponible_delivery = ?, disponible_local = ?, " +
                "stock = ?, requiere_inventario = ?, fecha_actualizacion = ? WHERE id_menu = ?";
        jdbcTemplate.update(sql,
                menu.getNombre(),
                menu.getDescripcion(),
                menu.getPrecio(),
                menu.getRuta_imagen(),
                menu.getDisponible(),
                menu.getCategoria().getId_categoria(),
                menu.getOrden(),
                menu.getCantidad_vendida(),
                menu.getTotal_ingresos(),
                menu.getEn_promocion(),
                menu.getPrecio_promocion(),
                menu.getFecha_inicio_promocion() != null ? new java.sql.Date(menu.getFecha_inicio_promocion().getTime()) : null,
                menu.getFecha_fin_promocion() != null ? new java.sql.Date(menu.getFecha_fin_promocion().getTime()) : null,
                menu.getEs_recomendado(),
                menu.getEs_nuevo(),
                menu.getEs_popular(),
                menu.getTiempo_preparacion(),
                menu.getDisponible_delivery(),
                menu.getDisponible_local(),
                menu.getStock(),
                menu.getRequiere_inventario(),
                new java.sql.Timestamp(new Date().getTime()),
                menu.getId_menu()
        );
    }

    @Override
    public int eliminarMenu(Long id) {
        String sql = "DELETE FROM menu WHERE id_menu = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Menu> listarDisponibles() {
        String sql = "SELECT * FROM menu WHERE disponible = true ORDER BY orden, nombre";
        return jdbcTemplate.query(sql, menuRowMapper);
    }

    @Override
    public List<Menu> listarPorCategoria(Long idCategoria) {
        String sql = "SELECT * FROM menu WHERE id_categoria = ? AND disponible = true ORDER BY orden, nombre";
        return jdbcTemplate.query(sql, menuRowMapper, idCategoria);
    }

    @Override
    public List<Menu> listarEnPromocion() {
        String sql = "SELECT * FROM menu WHERE en_promocion = true AND disponible = true ORDER BY orden, nombre";
        return jdbcTemplate.query(sql, menuRowMapper);
    }

    @Override
    public List<Menu> listarRecomendados() {
        String sql = "SELECT * FROM menu WHERE es_recomendado = true AND disponible = true ORDER BY orden, nombre";
        return jdbcTemplate.query(sql, menuRowMapper);
    }

    @Override
    public List<Menu> listarNuevos() {
        String sql = "SELECT * FROM menu WHERE es_nuevo = true AND disponible = true ORDER BY fecha_creacion DESC";
        return jdbcTemplate.query(sql, menuRowMapper);
    }

    @Override
    public List<Menu> listarPopulares() {
        String sql = "SELECT * FROM menu WHERE es_popular = true AND disponible = true ORDER BY cantidad_vendida DESC";
        return jdbcTemplate.query(sql, menuRowMapper);
    }

    @Override
    public List<Menu> listarMasVendidos(int limite) {
        String sql = "SELECT * FROM menu WHERE disponible = true ORDER BY cantidad_vendida DESC LIMIT ?";
        return jdbcTemplate.query(sql, menuRowMapper, limite);
    }

    @Override
    public List<Menu> listarPorIngresos(int limite) {
        String sql = "SELECT * FROM menu WHERE disponible = true ORDER BY total_ingresos DESC LIMIT ?";
        return jdbcTemplate.query(sql, menuRowMapper, limite);
    }

    @Override
    public List<Menu> buscarPorNombre(String nombre) {
        String sql = "SELECT * FROM menu WHERE nombre LIKE ? AND disponible = true ORDER BY nombre";
        return jdbcTemplate.query(sql, menuRowMapper, "%" + nombre + "%");
    }

    @Override
    public void actualizarVentas(Long id, int cantidad, Double total) {
        String sql = "UPDATE menu SET cantidad_vendida = cantidad_vendida + ?, total_ingresos = total_ingresos + ? WHERE id_menu = ?";
        jdbcTemplate.update(sql, cantidad, total, id);
    }

}