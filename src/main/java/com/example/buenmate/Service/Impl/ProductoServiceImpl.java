package com.example.buenmate.Service.Impl;

import com.example.buenmate.Model.Producto;
import com.example.buenmate.Repository.ProductoRepository;
import com.example.buenmate.Service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<Producto> listarTodos(){
        return productoRepository.findAll();
    }

    @Override
    public Producto guardar(Producto producto){
        producto.setId(null); // Asegurarse de que el ID sea nulo para crear un nuevo producto
        return productoRepository.save(producto);
    }

    @Override
    public Producto actualizar(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public Producto buscarPorId(Long id){

        return productoRepository.findById(id).orElse(null);
    }

    @Override
    public void eliminar(Long id) {
      productoRepository.deleteById(id);
    }

    }
