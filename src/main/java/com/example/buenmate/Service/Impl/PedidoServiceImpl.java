package com.example.buenmate.Service.Impl;


import org.springframework.transaction.annotation.Transactional;
import com.example.buenmate.Enums.EstadoPedido;
import com.example.buenmate.Model.ItemPedido;
import com.example.buenmate.Model.Pedido;
import com.example.buenmate.Model.Producto;
import com.example.buenmate.Model.Usuario;
import com.example.buenmate.Repository.PedidoRepository;
import com.example.buenmate.Repository.ProductoRepository;
import com.example.buenmate.Repository.UsuarioRepository;
import com.example.buenmate.Service.PedidoService;
import com.example.buenmate.dto.ItemPedidoRequest;
import com.example.buenmate.dto.ItemPedidoResponseDTO;
import com.example.buenmate.dto.PedidoRequest;
import com.example.buenmate.dto.PedidoResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;


    // Si falla cualquier parte, también se revierten los cambios de stock.
    @Transactional
    @Override
    public PedidoResponseDTO crearPedido(Long usuarioId, PedidoRequest pedidoRequest) {
        // 1. Buscamos al usuario que está comprando
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Creamos el Pedido vacío, con estado PENDIENTE por default
        Pedido pedido = new Pedido();

        pedido.setUsuario(usuario);
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setTotal(BigDecimal.ZERO); // usamos ZERO porque arrancamos de $0.00

        List<ItemPedido> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;


        // 1 EXTRAIGO LOS PRODUCTOS DE CADA ITEM DEL PEDIDO Y VERIFICO STOCK
        for (ItemPedidoRequest itemPedidoRequest : pedidoRequest.getItems()){
            Producto producto = productoRepository.findById(itemPedidoRequest.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado")) ;

        // 2 VERIFICO QUE EL STOCK SEA SUFICIENTE PARA LA CANTIDAD SOLICITADA
        if (producto.getStock() < itemPedidoRequest.getCantidad()){
            throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());

        // 3 SI HAY STOCK SUFICIENTE, DESCUENTO EL STOCK Y ACTUALIZO EL PRODUCTO
        } else {
            producto.setStock((producto.getStock())- itemPedidoRequest.getCantidad());
            productoRepository.save(producto);
        }

        // 4 CREO EL ITEM DEL PEDIDO Y LO AGREGO A LA LISTA DE ITEMS DEL PEDIDO
            ItemPedido item = new ItemPedido();
            item.setProducto(producto);
            item.setCantidad(itemPedidoRequest.getCantidad());
            item.setPrecioUnitario(producto.getPrecio());
            //
            item.setPedido(pedido); // seteo el pedido a item para que se guarde la relación en la base de datos


         // 5. Le pego la lista de items y el total real al pedido, y lo guardo en la base
            items.add(item);
            total = total.add(producto.getPrecio().multiply(BigDecimal.valueOf(itemPedidoRequest.getCantidad())));

        }

        // 6. Seteo los items y el total al pedido
        pedido.setItems(items);
        pedido.setTotal(total);

        Pedido pedidoGuardado = pedidoRepository.save(pedido);


        // 7. Mapeo el pedido a PedidoResponseDTO para devolverlo al cliente
        PedidoResponseDTO pedidoResponseDTO = new PedidoResponseDTO();

        pedidoResponseDTO.setId(pedidoGuardado.getId());
        pedidoResponseDTO.setEstado(pedidoGuardado.getEstado().name());
        pedidoResponseDTO.setFecha(pedidoGuardado.getFecha());
        pedidoResponseDTO.setTotal(pedidoGuardado.getTotal());
        pedidoResponseDTO.setUsuarioNombre(usuario.getNombre());
        pedidoResponseDTO.setUsuarioEmail(usuario.getEmail());

        // Mapeo los items del pedido a ItemPedidoResponseDTO y los agrego a la respuesta
        pedidoResponseDTO.setItems(items.stream().map(itemPedido -> {

            ItemPedidoResponseDTO dto = new ItemPedidoResponseDTO();
            dto.setProductoId(itemPedido.getProducto().getId());
            dto.setCantidad(itemPedido.getCantidad());
            dto.setPrecioUnitario(itemPedido.getPrecioUnitario());
            dto.setProductoNombre(itemPedido.getProducto().getNombre());
            return dto;
        }).collect(Collectors.toList()));


        return pedidoResponseDTO;


    }

    private PedidoResponseDTO mapearPedido(Pedido pedido) {
        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setId(pedido.getId());
        dto.setFecha(pedido.getFecha());
        dto.setEstado(pedido.getEstado().name());
        dto.setTotal(pedido.getTotal());
        dto.setUsuarioNombre(pedido.getUsuario().getNombre());
        dto.setUsuarioEmail(pedido.getUsuario().getEmail());
        dto.setItems(pedido.getItems().stream()
                .map(this::mapearItem)
                .collect(Collectors.toList()));
        return dto;
    }

    private ItemPedidoResponseDTO mapearItem(ItemPedido item) {
        ItemPedidoResponseDTO dto = new ItemPedidoResponseDTO();
        dto.setProductoId(item.getProducto().getId());
        dto.setProductoNombre(item.getProducto().getNombre());
        dto.setCantidad(item.getCantidad());
        dto.setPrecioUnitario(item.getPrecioUnitario());
        return dto;
    }

    @Override
    public List<PedidoResponseDTO> listarPedidos() {
        return pedidoRepository.findAll()
                .stream()
                .map(this::mapearPedido)
                .collect(Collectors.toList());
    }

    @Override
    public List<PedidoResponseDTO> obtenerPedidosPorEstado(EstadoPedido estado){
        return pedidoRepository.findByEstado(estado)
                .stream()
                .map(this::mapearPedido)
                .collect(Collectors.toList());
    }



    @Override
    public PedidoResponseDTO cambiarEstadoPedido(Long pedidoId, EstadoPedido estado) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        pedido.setEstado(estado);
        Pedido pedidoActualizado = pedidoRepository.save(pedido);
        return mapearPedido(pedidoActualizado);
    }


    @Override
    public List<PedidoResponseDTO> obtenerPedidosPorUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId) // busca los pedidos por el ID del usuario
                .stream() // convierte la lista de pedidos en un stream
                .map(this::mapearPedido) // mapea cada pedido a un PedidoResponseDTO
                .collect(Collectors.toList()); // y finalmente lo convierte de nuevo en una lista
    }

}


