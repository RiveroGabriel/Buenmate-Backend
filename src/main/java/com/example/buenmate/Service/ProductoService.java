package com.example.buenmate.Service;

import com.example.buenmate.Model.Producto;

import java.util.List;

public interface ProductoService {

    List<Producto> listarTodos();
    List<Producto> listarActivos();
    Producto guardar(Producto producto);
    Producto actualizar(Producto producto);
    Producto buscarPorId(Long id);
    void eliminar(Long id);
}
