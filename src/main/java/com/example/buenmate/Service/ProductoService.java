package com.example.buenmate.Service;

import com.example.buenmate.Model.Producto;

import java.util.List;

public interface ProductoService {

    List<Producto> listarTodos();
    Producto guardar(Producto producto);
    Producto buscarPorId(Long id);
    void elimiar(Long id);
}
