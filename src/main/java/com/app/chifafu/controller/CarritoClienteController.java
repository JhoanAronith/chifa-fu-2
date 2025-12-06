package com.app.chifafu.controller;

import com.app.chifafu.model.*;
import com.app.chifafu.security.CustomUserDetails;
import com.app.chifafu.service.CarritoService;
import com.app.chifafu.service.ClienteService;
import com.app.chifafu.service.DetalleCarritoService;
import com.app.chifafu.service.MenuService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/chifafu/cliente/carrito")
public class CarritoClienteController {

    private final CarritoService carritoService;
    private final ClienteService clienteService;
    private final DetalleCarritoService detalleService;
    private final MenuService menuService;

    public CarritoClienteController(CarritoService carritoService,
                                    ClienteService clienteService,
                                    DetalleCarritoService detalleService,
                                    MenuService menuService) {
        this.carritoService = carritoService;
        this.clienteService = clienteService;
        this.detalleService = detalleService;
        this.menuService = menuService;
    }

    //Mostrar carrito del cliente
    @GetMapping("/{idCliente}")
    public String mostrarCarrito(@PathVariable Long idCliente,
                                 Model model,
                                 @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (!userDetails.getUsuario().getCliente().getId_cliente().equals(idCliente)) {
            return "redirect:/error";
        }

        Carrito carrito = carritoService.obtenerOCrear(idCliente);

        List<DetalleCarrito> detalles =
                detalleService.listarPorCarrito(carrito.getId_carrito());

        double subtotal = detalles.stream()
                .mapToDouble(d -> d.getPrecio_unitario() * d.getCantidad())
                .sum();

        model.addAttribute("carrito", carrito);
        model.addAttribute("detalles", detalles);
        model.addAttribute("subtotal", subtotal);

        return "cliente/carrito";
    }

    //Agregar productos al carrito
    @PostMapping("/{idCliente}/agregar")
    public String agregarProducto(
            @PathVariable Long idCliente,
            @RequestParam Long idMenu,
            @RequestParam(defaultValue = "1") Integer cantidad,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (!userDetails.getUsuario().getCliente().getId_cliente().equals(idCliente)) {
            return "redirect:/error";
        }

        Carrito carrito = carritoService.obtenerOCrear(idCliente);
        Menu menu = menuService.obtenerPorId(idMenu).orElse(null);

        if (menu == null) return "redirect:/error";

        List<DetalleCarrito> detalles =
                detalleService.listarPorCarrito(carrito.getId_carrito());

        for (DetalleCarrito d : detalles) {
            if (d.getMenu().getId_menu().equals(idMenu)) {
                d.setCantidad(d.getCantidad() + cantidad);
                detalleService.actualizar(d);
                return "redirect:/chifafu/cliente/carrito/" + idCliente;
            }
        }

        DetalleCarrito nuevo = new DetalleCarrito();
        nuevo.setCarrito(carrito);
        nuevo.setMenu(menu);
        nuevo.setCantidad(cantidad);
        nuevo.setPrecio_unitario(menu.getPrecioFinal());

        detalleService.crear(nuevo);

        return "redirect:/chifafu/cliente/carrito/" + idCliente;
    }


    // Actualizar la cantidad de un item
    @PostMapping("/{idDetalle}/actualizar")
    public String actualizarCantidad(
            @PathVariable Long idDetalle,
            @RequestParam Long idCliente,
            @RequestParam Integer cantidad,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails.getUsuario().getCliente() == null) {
            return "redirect:/chifafu/cliente/perfil";
        }

        if (!userDetails.getUsuario().getCliente().getId_cliente().equals(idCliente)) {
            return "redirect:/error";
        }

        if (cantidad < 1) {
            return "redirect:/chifafu/cliente/carrito/" + idCliente;
        }

        detalleService.obtenerPorId(idDetalle).ifPresent(detalle -> {
            detalle.setCantidad(cantidad);
            detalleService.actualizar(detalle);
        });

        return "redirect:/chifafu/cliente/carrito/" + idCliente;
    }

    // Eliminar un item del carrito
    @PostMapping("/detalle/{idDetalle}/eliminar")
    public String eliminarDetalle(@PathVariable Long idDetalle, @RequestParam Long idCliente) {
        detalleService.eliminar(idDetalle);
        return "redirect:/chifafu/cliente/carrito/" + idCliente;
    }

}
