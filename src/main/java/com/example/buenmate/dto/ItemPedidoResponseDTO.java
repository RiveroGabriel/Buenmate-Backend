package com.example.buenmate.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ItemPedidoResponseDTO {
    private Long productoId;
    private String productoNombre;
    private Integer cantidad;
    private BigDecimal precioUnitario;
}
