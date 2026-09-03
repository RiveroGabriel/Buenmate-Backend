package com.example.buenmate.Service.Impl;


import com.example.buenmate.Model.DatosTransferencia;
import com.example.buenmate.Repository.DatosTransferenciaRepository;
import com.example.buenmate.Service.DatosTransferenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DatosTransferenciaServiceImpl implements DatosTransferenciaService {

    @Autowired
    private DatosTransferenciaRepository datosTransferenciaRepository;

    @Override
    public DatosTransferencia guardar(DatosTransferencia datosTransferencia) {

        Optional<DatosTransferencia> existDatosTransferencia =
                datosTransferenciaRepository.findFirstByOrderByIdAsc();

        // Pregunto si existe un registro previo de DatosTransferencia
        if(existDatosTransferencia.isPresent()){
           datosTransferencia.setId(existDatosTransferencia.get().getId());
        }
        return datosTransferenciaRepository.save(datosTransferencia);
    }

    @Override
    public DatosTransferencia obtenerDatosTransferencia() {
        return datosTransferenciaRepository.findFirstByOrderByIdAsc().orElse(null);
    }


}
