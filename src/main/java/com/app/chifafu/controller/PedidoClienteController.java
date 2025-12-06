package com.app.chifafu.controller;

import com.app.chifafu.model.*;
import com.app.chifafu.security.CustomUserDetails;
import com.app.chifafu.service.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/chifafu/cliente/pedido")
public class PedidoClienteController {

    private final PedidoService pedidoService;
    private final DetallePedidoService detallePedidoService;
    private final CarritoService carritoService;
    private final DetalleCarritoService detalleCarritoService;
    private final ClienteService clienteService;
    private final LocalService localService;
    private final DireccionService direccionService;
    private final PagoService pagoService;

    public PedidoClienteController(PedidoService pedidoService,
                                   DetallePedidoService detallePedidoService,
                                   CarritoService carritoService,
                                   DetalleCarritoService detalleCarritoService,
                                   ClienteService clienteService,
                                   LocalService localService,
                                   DireccionService direccionService,
                                   PagoService pagoService) {
        this.pedidoService = pedidoService;
        this.detallePedidoService = detallePedidoService;
        this.carritoService = carritoService;
        this.detalleCarritoService = detalleCarritoService;
        this.clienteService = clienteService;
        this.localService = localService;
        this.direccionService = direccionService;
        this.pagoService = pagoService;
    }

    //Crear un nuevo pedido usando los datos del carrito
    @PostMapping("/crear")
    public String crearPedidoDesdeCarrito(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        if (userDetails.getUsuario().getCliente() == null) {
            return "redirect:/chifafu/cliente/perfil";
        }

        Long idCliente = userDetails.getUsuario().getCliente().getId_cliente();
        Optional<Cliente> clienteOpt = clienteService.obtenerPorId(idCliente);

        if (clienteOpt.isEmpty()) {
            return "redirect:/error";
        }

        Cliente cliente = clienteOpt.get();
        Carrito carrito = carritoService.obtenerOCrear(idCliente);
        List<DetalleCarrito> detallesCarrito = detalleCarritoService.listarPorCarrito(carrito.getId_carrito());

        if (detallesCarrito.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El carrito está vacío");
            return "redirect:/chifafu/cliente/carrito/" + idCliente;
        }

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setEstado(Pedido.EstadoPedido.PENDIENTE);
        pedido.setTipo_entrega(Pedido.TipoEntrega.DELIVERY);

        double total = detallesCarrito.stream()
                .mapToDouble(d -> d.getPrecio_unitario() * d.getCantidad())
                .sum();
        pedido.setTotal(total);

        Pedido pedidoCreado = pedidoService.crear(pedido);

        for (DetalleCarrito detalleCarrito : detallesCarrito) {
            DetallePedido detallePedido = new DetallePedido();
            detallePedido.setPedido(pedidoCreado);
            detallePedido.setMenu(detalleCarrito.getMenu());
            detallePedido.setCantidad(detalleCarrito.getCantidad());
            detallePedido.setPrecio_unitario(detalleCarrito.getPrecio_unitario());
            detallePedido.calcularSubtotal();

            detallePedidoService.crear(detallePedido);
        }

        detalleCarritoService.eliminarPorCarrito(carrito.getId_carrito());

        redirectAttributes.addFlashAttribute("success", "Pedido creado. Completa la información");
        return "redirect:/chifafu/cliente/pedido/" + pedidoCreado.getId_pedido() + "/completar";
    }

    //Mostrar página para completar los datos del pedido
    @GetMapping("/{idPedido}/completar")
    public String mostrarCompletarPedido(
            @PathVariable Long idPedido,
            Model model,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails.getUsuario().getCliente() == null) {
            return "redirect:/chifafu/cliente/perfil";
        }

        Long idCliente = userDetails.getUsuario().getCliente().getId_cliente();

        Optional<Pedido> pedidoOpt = pedidoService.obtenerPorId(idPedido);
        if (pedidoOpt.isEmpty() || !pedidoOpt.get().getCliente().getId_cliente().equals(idCliente)) {
            return "redirect:/error";
        }

        Pedido pedido = pedidoOpt.get();
        if (pedido.getEstado() != Pedido.EstadoPedido.PENDIENTE) {
            return "redirect:/chifafu/cliente/pedido/" + idPedido;
        }

        List<DetallePedido> detalles = detallePedidoService.listarPorPedido(idPedido);
        Double total = detallePedidoService.calcularTotalPedido(idPedido);
        List<Local> locales = localService.listarLocales();

        model.addAttribute("pedido", pedido);
        model.addAttribute("detalles", detalles);
        model.addAttribute("total", total);
        model.addAttribute("locales", locales);
        model.addAttribute("tiposEntrega", Pedido.TipoEntrega.values());

        return "cliente/pedido/completar-pedido";
    }

    //Actualizar entrega de un pedido
    @PostMapping("/{idPedido}/actualizar-entrega")
    public String actualizarEntrega(
            @PathVariable Long idPedido,
            @RequestParam Pedido.TipoEntrega tipo_entrega,
            @RequestParam(required = false) Long idLocal,
            @RequestParam(required = false) String calle,
            @RequestParam(required = false) String numero,
            @RequestParam(required = false) String distrito,
            @RequestParam(required = false) String referencia,
            @RequestParam(required = false) String telefono_contacto,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        if (userDetails.getUsuario().getCliente() == null) {
            return "redirect:/chifafu/cliente/perfil";
        }

        Long idCliente = userDetails.getUsuario().getCliente().getId_cliente();
        Optional<Pedido> pedidoOpt = pedidoService.obtenerPorId(idPedido);

        if (pedidoOpt.isEmpty() || !pedidoOpt.get().getCliente().getId_cliente().equals(idCliente)) {
            return "redirect:/error";
        }

        Pedido pedido = pedidoOpt.get();

        if (pedido.getEstado() != Pedido.EstadoPedido.PENDIENTE) {
            return "redirect:/chifafu/cliente/pedido/" + idPedido;
        }

        pedido.setTipo_entrega(tipo_entrega);

        if (tipo_entrega == Pedido.TipoEntrega.RECOJO_LOCAL) {
            if (idLocal == null || idLocal <= 0) {
                redirectAttributes.addFlashAttribute("error", "Debes seleccionar un local");
                return "redirect:/chifafu/cliente/pedido/" + idPedido + "/completar";
            }
            Optional<Local> localOpt = localService.obtenerPorId(idLocal);
            if (localOpt.isEmpty()) {
                return "redirect:/error";
            }
            pedido.setLocal(localOpt.get());
            pedido.setDireccion(null);

        } else if (tipo_entrega == Pedido.TipoEntrega.DELIVERY) {
            if (calle == null || calle.isEmpty() || distrito == null || distrito.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "La calle y distrito son requeridos");
                return "redirect:/chifafu/cliente/pedido/" + idPedido + "/completar";
            }

            Direccion direccion = new Direccion();
            direccion.setCalle(calle);
            direccion.setNumero(numero);
            direccion.setDistrito(distrito);
            direccion.setReferencia(referencia);
            direccion.setTelefono_contacto(telefono_contacto);

            Direccion direccionCreada = direccionService.crear(direccion);
            pedido.setDireccion(direccionCreada);
            pedido.setLocal(null);
        }

        pedidoService.actualizar(pedido);
        redirectAttributes.addFlashAttribute("success", "Información de entrega actualizada");

        return "redirect:/chifafu/cliente/pedido/" + idPedido + "/pago";
    }

    //Mostrar la página para realizar el pago
    @GetMapping("/{idPedido}/pago")
    public String mostrarPago(
            @PathVariable Long idPedido,
            Model model,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails.getUsuario().getCliente() == null) {
            return "redirect:/chifafu/cliente/perfil";
        }

        Long idCliente = userDetails.getUsuario().getCliente().getId_cliente();
        Optional<Pedido> pedidoOpt = pedidoService.obtenerPorId(idPedido);

        if (pedidoOpt.isEmpty() || !pedidoOpt.get().getCliente().getId_cliente().equals(idCliente)) {
            return "redirect:/error";
        }

        Pedido pedido = pedidoOpt.get();

        if ((pedido.getTipo_entrega() == Pedido.TipoEntrega.DELIVERY && pedido.getDireccion() == null) ||
                (pedido.getTipo_entrega() == Pedido.TipoEntrega.RECOJO_LOCAL && pedido.getLocal() == null)) {
            return "redirect:/chifafu/cliente/pedido/" + idPedido + "/completar";
        }

        List<DetallePedido> detalles = detallePedidoService.listarPorPedido(idPedido);
        Double total = detallePedidoService.calcularTotalPedido(idPedido);

        model.addAttribute("pedido", pedido);
        model.addAttribute("detalles", detalles);
        model.addAttribute("total", total);
        model.addAttribute("metodosPago", Pago.MetodoPago.values());

        return "cliente/pedido/pago";
    }

    //Procesar el pago de un pedido
    @PostMapping("/{idPedido}/procesar-pago")
    public String procesarPago(
            @PathVariable Long idPedido,
            @RequestParam Pago.MetodoPago metodo_pago,
            @RequestParam(required = false) String numero_transaccion,
            @RequestParam(required = false) String comprobante,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        if (userDetails.getUsuario().getCliente() == null) {
            return "redirect:/chifafu/cliente/perfil";
        }

        Long idCliente = userDetails.getUsuario().getCliente().getId_cliente();
        Optional<Pedido> pedidoOpt = pedidoService.obtenerPorId(idPedido);

        if (pedidoOpt.isEmpty() || !pedidoOpt.get().getCliente().getId_cliente().equals(idCliente)) {
            return "redirect:/error";
        }

        Pedido pedido = pedidoOpt.get();

        if (pedido.getEstado() != Pedido.EstadoPedido.PENDIENTE) {
            redirectAttributes.addFlashAttribute("error", "Este pedido ya fue procesado");
            return "redirect:/chifafu/cliente/pedido/" + idPedido;
        }

        Pago pago = new Pago();
        pago.setMonto(pedido.getTotal());
        pago.setMetodo_pago(metodo_pago);
        pago.setNumero_transaccion(numero_transaccion);
        pago.setComprobante(comprobante);
        pago.setEstado_pago(Pago.EstadoPago.COMPLETADO);

        Pago pagoCreado = pagoService.crear(pago);
        pedido.setPago(pagoCreado);
        pedido.setEstado(Pedido.EstadoPedido.CONFIRMADO);

        pedidoService.actualizar(pedido);

        redirectAttributes.addFlashAttribute("success", "¡Pedido confirmado exitosamente!");
        return "redirect:/chifafu/cliente/pedido/" + idPedido;
    }

    //Ver el pedido por su id
    @GetMapping("/{idPedido}")
    public String verPedido(
            @PathVariable Long idPedido,
            Model model,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails.getUsuario().getCliente() == null) {
            return "redirect:/chifafu/cliente/perfil";
        }

        Long idCliente = userDetails.getUsuario().getCliente().getId_cliente();
        Optional<Pedido> pedidoOpt = pedidoService.obtenerPorId(idPedido);

        if (pedidoOpt.isEmpty() || !pedidoOpt.get().getCliente().getId_cliente().equals(idCliente)) {
            return "redirect:/error";
        }

        Pedido pedido = pedidoOpt.get();
        List<DetallePedido> detalles = detallePedidoService.listarPorPedido(idPedido);
        Double total = detallePedidoService.calcularTotalPedido(idPedido);

        model.addAttribute("pedido", pedido);
        model.addAttribute("detalles", detalles);
        model.addAttribute("total", total);

        return "cliente/pedido/ver-pedido";
    }

    //Ver todos los pedidos de un usuario
    @GetMapping("/mis-pedidos")
    public String misPedidos(
            Model model,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails.getUsuario().getCliente() == null) {
            return "redirect:/chifafu/cliente/perfil";
        }

        Long idCliente = userDetails.getUsuario().getCliente().getId_cliente();
        List<Pedido> pedidos = pedidoService.obtenerPorCliente(idCliente);

        model.addAttribute("pedidos", pedidos);

        return "cliente/pedido/mis-pedidos";
    }
}