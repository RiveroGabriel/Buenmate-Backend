package com.example.buenmate.Service;

import com.example.buenmate.Enums.EstadoPedido;
import com.example.buenmate.Model.Pedido;
import com.example.buenmate.dto.PedidoRequest;
import com.example.buenmate.dto.PedidoResponseDTO;

import java.util.List;

public interface PedidoService {

   PedidoResponseDTO crearPedido(Long usuario, PedidoRequest pedidoRequest);

    List<PedidoResponseDTO> listarPedidos();

    List<PedidoResponseDTO> obtenerPedidosPorEstado(EstadoPedido estado);

   PedidoResponseDTO cambiarEstadoPedido(Long PedidoId, EstadoPedido nuevoEstado);
 }
