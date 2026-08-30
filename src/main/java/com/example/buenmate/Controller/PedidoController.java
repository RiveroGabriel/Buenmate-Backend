package com.example.buenmate.Controller;


import com.example.buenmate.Model.Pedido;
import com.example.buenmate.Model.Usuario;
import com.example.buenmate.Repository.UsuarioRepository;
import com.example.buenmate.Service.PedidoService;
import com.example.buenmate.dto.PedidoRequest;
import com.example.buenmate.dto.PedidoResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public PedidoResponseDTO crearPedido(@RequestBody PedidoRequest pedidoRequest, Authentication authentication) {
        // "authentication.getName()" nos da el email del usuario logueado (viene del token JWT)
        String email = authentication.getName();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return pedidoService.crearPedido(usuario.getId(), pedidoRequest);


    }


}
