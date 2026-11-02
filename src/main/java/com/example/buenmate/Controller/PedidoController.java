package com.example.buenmate.Controller;


import com.example.buenmate.Enums.EstadoPedido;
import com.example.buenmate.Model.Usuario;
import com.example.buenmate.Repository.UsuarioRepository;
import com.example.buenmate.Service.PedidoService;
import com.example.buenmate.dto.PedidoRequest;
import com.example.buenmate.dto.PedidoResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private UsuarioRepository usuarioRepository;


    @PostMapping("/api/pedidos")
    public PedidoResponseDTO crearPedido(@RequestBody PedidoRequest pedidoRequest, Authentication authentication) {
        // "authentication.getName()" nos da el email del usuario logueado (viene del token JWT)
        String email = authentication.getName();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return pedidoService.crearPedido(usuario.getId(), pedidoRequest);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/api/admin/pedidos")
    public List<PedidoResponseDTO> listarPedidos(@RequestParam (required = false) EstadoPedido estado) {

        if(estado != null){
            return pedidoService.obtenerPedidosPorEstado(estado);
        }
        return pedidoService.listarPedidos();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/api/admin/pedidos/{id}/estado")
    public PedidoResponseDTO cambiarEstado(@PathVariable Long id, @RequestParam EstadoPedido estadoPedido){
        return pedidoService.cambiarEstadoPedido(id, estadoPedido);
    }

    @GetMapping("/api/pedidos/mis-pedidos")
    public List<PedidoResponseDTO> misPedidos(Authentication authentication) {
        String email = authentication.getName();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return pedidoService.obtenerPedidosPorUsuario(usuario.getId());
    }

}
