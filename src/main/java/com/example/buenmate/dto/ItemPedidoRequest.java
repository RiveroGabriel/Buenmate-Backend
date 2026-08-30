package com.example.buenmate.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemPedidoRequest {
    private Long productoId;
    private Integer cantidad;
}
