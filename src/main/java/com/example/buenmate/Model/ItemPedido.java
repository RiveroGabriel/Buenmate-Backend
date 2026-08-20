package com.example.buenmate.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "items_pedido")
@Data
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Muchos ItemPedido pueden pertenecer a UN mismo Pedido
    @ManyToOne
    // "pedido_id" es el nombre de la columna en MySQL que guarda a qué Pedido pertenece esta fila (la clave foránea)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    // Muchos ItemPedido pueden referenciar al mismo Producto (ej: 50 personas compraron el mismo mate)
    @ManyToOne
    @JoinColumn(name = "producto_id") // columna que guarda a qué Producto referencia esta fila
    private Producto producto;

    private Integer cantidad; // cuántas unidades de ese producto se pidieron

    private BigDecimal precioUnitario; // el precio al momento de la compra, no el actual (por si el precio cambia después)
}