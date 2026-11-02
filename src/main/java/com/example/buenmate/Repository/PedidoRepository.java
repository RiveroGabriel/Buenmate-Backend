package com.example.buenmate.Repository;

import com.example.buenmate.Enums.EstadoPedido;
import com.example.buenmate.Model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {


    List<Pedido> findByEstado(EstadoPedido estado);

    List<Pedido> findByUsuarioId(Long usuarioId); // Obtener pedidos por ID de usuario

}
