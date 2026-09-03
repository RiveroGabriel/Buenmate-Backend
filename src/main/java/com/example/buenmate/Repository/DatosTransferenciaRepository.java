package com.example.buenmate.Repository;

import com.example.buenmate.Model.DatosTransferencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DatosTransferenciaRepository extends JpaRepository<DatosTransferencia,Long> {

    Optional<DatosTransferencia> findFirstByOrderByIdAsc(); // Traigo el unico registro de la tabla DatosTransferencia

}
