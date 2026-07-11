package com.example.buenmate.Model;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "productos")
@Data
public class Producto {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     @Column(nullable = false)
     private String nombre;

     @Column(length = 500)
     private String descripcion;

     @Column(nullable = false)
     private BigDecimal precio;

     @Column(nullable = false)
     private Integer stock;

     private String imagenUrl;

     @Column(nullable = false)
     private Boolean activo = true;

     @ManyToOne // Muchos a uno
     @JoinColumn(name = "categoria_id")
     private Categoria categoria;




}
