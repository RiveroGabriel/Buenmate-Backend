package com.example.buenmate.Service.Impl;

import com.example.buenmate.Model.Categoria;
import com.example.buenmate.Repository.CategoriaRepository;
import com.example.buenmate.Service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService  {



    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria guardar(Categoria categoria){
        return categoriaRepository.save(categoria);
    }

    @Override
    public Categoria buscarPorId(Long id){
        return categoriaRepository.findById(id).orElse(null);
    }

    @Override
    public void eliminar(Long id){
        categoriaRepository.deleteById(id);
    }

}

