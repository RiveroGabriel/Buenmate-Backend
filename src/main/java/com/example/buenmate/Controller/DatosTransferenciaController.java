package com.example.buenmate.Controller;


import com.example.buenmate.Model.DatosTransferencia;
import com.example.buenmate.Repository.DatosTransferenciaRepository;
import com.example.buenmate.Service.DatosTransferenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class DatosTransferenciaController {

    @Autowired
    private DatosTransferenciaService datosTransferenciaService;

    @GetMapping("/api/datos-transferencia")
    public DatosTransferencia obtenerDatosTransferencia() {
        return datosTransferenciaService.obtenerDatosTransferencia();
    }


    @PutMapping("/api/admin/datos-transferencia")
    public DatosTransferencia guardarDatosTransferencia( @RequestBody DatosTransferencia datos){
        return datosTransferenciaService.guardar(datos);
    }



}
