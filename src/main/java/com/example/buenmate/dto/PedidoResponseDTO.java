package com.example.buenmate.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PedidoResponseDTO {
    private Long id;
    private LocalDateTime fecha;
    private String estado;
    private BigDecimal total;
    private String usuarioNombre;
    private String usuarioEmail;
    private List<ItemPedidoResponseDTO> items ;
}
