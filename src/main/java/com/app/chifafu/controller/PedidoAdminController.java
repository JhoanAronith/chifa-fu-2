package com.app.chifafu.controller;

import com.app.chifafu.model.Pedido;
import com.app.chifafu.service.PedidoService;
import com.app.chifafu.service.DetallePedidoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/chifafu/admin/pedido")
public class PedidoAdminController {

    private final PedidoService pedidoService;
    private final DetallePedidoService detallePedidoService;

    public PedidoAdminController(PedidoService pedidoService,
                                 DetallePedidoService detallePedidoService) {
        this.pedidoService = pedidoService;
        this.detallePedidoService = detallePedidoService;
    }

    // ========== LISTAR TODOS LOS PEDIDOS ==========
    @GetMapping("/todos")
    public String listarTodosPedidos(Model model,
                                     @RequestParam(required = false) Pedido.EstadoPedido estado) {

        List<Pedido> pedidos;

        if (estado != null) {
            pedidos = pedidoService.obtenerPorEstado(estado);
            model.addAttribute("estadoFiltro", estado);
        } else {
            pedidos = pedidoService.obtenerTodos();
        }

        model.addAttribute("pedidos", pedidos);
        model.addAttribute("estados", Pedido.EstadoPedido.values());

        return "administrador/pedido/todos-pedidos";
    }

    // ========== VER DETALLES DE UN PEDIDO ==========
    @GetMapping("/{idPedido}")
    public String verPedido(@PathVariable Long idPedido, Model model) {

        Optional<Pedido> pedidoOpt = pedidoService.obtenerPorId(idPedido);

        if (pedidoOpt.isEmpty()) {
            return "redirect:/chifafu/admin/pedido/todos";
        }

        Pedido pedido = pedidoOpt.get();
        Double total = detallePedidoService.calcularTotalPedido(idPedido);

        model.addAttribute("pedido", pedido);
        model.addAttribute("detalles", detallePedidoService.listarPorPedido(idPedido));
        model.addAttribute("total", total);
        model.addAttribute("estados", Pedido.EstadoPedido.values());

        return "administrador/pedido/ver-pedido-admin";
    }

    // ========== CAMBIAR ESTADO DEL PEDIDO ==========
    @PostMapping("/{idPedido}/cambiar-estado")
    public String cambiarEstado(@PathVariable Long idPedido,
                                @RequestParam Pedido.EstadoPedido nuevoEstado,
                                RedirectAttributes redirectAttributes) {

        Optional<Pedido> pedidoOpt = pedidoService.obtenerPorId(idPedido);

        if (pedidoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Pedido no encontrado");
            return "redirect:/chifafu/admin/pedido/todos";
        }

        Pedido pedido = pedidoOpt.get();
        String estadoAnterior = pedido.getEstado().toString();
        pedido.setEstado(nuevoEstado);

        pedidoService.actualizar(pedido);

        redirectAttributes.addFlashAttribute("success",
                "Estado actualizado de " + estadoAnterior + " a " + nuevoEstado);

        return "redirect:/chifafu/admin/pedido/" + idPedido;
    }

    // ========== AGREGAR NOTAS AL PEDIDO ==========
    @PostMapping("/{idPedido}/agregar-notas")
    public String agregarNotas(@PathVariable Long idPedido,
                               @RequestParam String notas,
                               RedirectAttributes redirectAttributes) {

        Optional<Pedido> pedidoOpt = pedidoService.obtenerPorId(idPedido);

        if (pedidoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Pedido no encontrado");
            return "redirect:/chifafu/admin/pedido/todos";
        }

        Pedido pedido = pedidoOpt.get();
        pedido.setNotas(notas);

        pedidoService.actualizar(pedido);

        redirectAttributes.addFlashAttribute("success", "Notas agregadas correctamente");

        return "redirect:/chifafu/admin/pedido/" + idPedido;
    }

    // ========== BUSCAR PEDIDO POR NÚMERO ==========
    @GetMapping("/buscar")
    public String buscarPedido(@RequestParam String numeroPedido,
                               Model model) {

        Optional<Pedido> pedidoOpt = pedidoService.obtenerPorNumeroPedido(numeroPedido);

        if (pedidoOpt.isEmpty()) {
            model.addAttribute("error", "Pedido no encontrado");
            model.addAttribute("pedidos", pedidoService.obtenerTodos());
            model.addAttribute("estados", Pedido.EstadoPedido.values());
            return "administrador/pedido/todos-pedidos";
        }

        return "redirect:/chifafu/admin/pedido/" + pedidoOpt.get().getId_pedido();
    }

    // ========== FILTRAR POR ESTADO ==========
    @GetMapping("/filtrar")
    public String filtrarPorEstado(@RequestParam Pedido.EstadoPedido estado,
                                   Model model) {

        List<Pedido> pedidos = pedidoService.obtenerPorEstado(estado);

        model.addAttribute("pedidos", pedidos);
        model.addAttribute("estadoFiltro", estado);
        model.addAttribute("estados", Pedido.EstadoPedido.values());

        return "administrador/pedido/todos-pedidos";
    }
}