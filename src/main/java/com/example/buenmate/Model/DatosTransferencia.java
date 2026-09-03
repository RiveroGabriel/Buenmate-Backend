package com.example.buenmate.Model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "datos_transferencia")
@Data
public class DatosTransferencia {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cbu;

    @Column(nullable = false)
    private String alias;

    @Column(nullable = false)
    private String numeroWhatsapp;



}
