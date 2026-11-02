package com.example.buenmate.Controller;


import com.example.buenmate.Model.Producto;
import com.example.buenmate.Service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Producto> listarTodos(){ // Devuelve todos los productos, tanto activos como inactivos
        return productoService.listarTodos();
    }

    // Listar productos activos
    @GetMapping("/activos")
    public List<Producto> listarActivos(){
        return productoService.listarActivos();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public Producto buscarPorId(@PathVariable Long id){
        return productoService.buscarPorId(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Producto actualizarProducto(@PathVariable Long id,@RequestBody Producto producto){
        producto.setId(id);
        return productoService.actualizar(producto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Producto guardar(@RequestBody Producto producto){
        return productoService.guardar(producto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/activar")
    public Producto activar(@PathVariable Long id){
        Producto producto = productoService.buscarPorId(id);
        producto.setActivo(true);
        return productoService.actualizar(producto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/desactivar")
    public Producto desactivar(@PathVariable Long id){
        Producto producto = productoService.buscarPorId(id);
        producto.setActivo(false);
        return productoService.actualizar(producto);
    }

}
