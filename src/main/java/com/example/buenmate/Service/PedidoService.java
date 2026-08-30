package com.example.buenmate.Service;

import com.example.buenmate.Model.Pedido;
import com.example.buenmate.dto.PedidoRequest;
import com.example.buenmate.dto.PedidoResponseDTO;

public interface PedidoService {
   PedidoResponseDTO crearPedido(Long usuario, PedidoRequest pedidoRequest);
}
