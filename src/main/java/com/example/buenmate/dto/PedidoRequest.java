package com.example.buenmate.dto;

import lombok.Data;

import java.util.List;

@Data
public class PedidoRequest {
   private List<ItemPedidoRequest> items;
}
