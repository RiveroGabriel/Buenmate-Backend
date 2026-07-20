package com.example.buenmate.Service;

import com.example.buenmate.Model.Categoria;
import com.example.buenmate.Repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CategoriaService {

    List<Categoria> listarTodas();
    Categoria guardar(Categoria categoria);
    Categoria buscarPorId(Long id);
    void eliminar(Long id);
}
