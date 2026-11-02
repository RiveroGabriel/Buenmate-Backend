package com.example.buenmate.Controller;


import com.example.buenmate.Model.Categoria;
import com.example.buenmate.Service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {


    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public List<Categoria> litarTodas(){
        return categoriaService.listarTodas();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public Categoria buscarPorId(@PathVariable Long id){
        return categoriaService.buscarPorId(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Categoria gurdar(@RequestBody Categoria categoria){
        return categoriaService.guardar(categoria);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id){
        categoriaService.eliminar(id);
    }


}
