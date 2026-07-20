package com.example.buenmate.Controller;


import com.example.buenmate.Model.Producto;
import com.example.buenmate.Service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<Producto> listarTodos(){
        return productoService.listarTodos();
    }

    @GetMapping("/{id}")
    public Producto buscarPorId(@PathVariable Long id){
        return productoService.buscarPorId(id);
    }

    @PostMapping
    public Producto guardar(@RequestBody Producto producto){
        return productoService.guardar(producto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id){
        productoService.elimiar(id);
    }

}
