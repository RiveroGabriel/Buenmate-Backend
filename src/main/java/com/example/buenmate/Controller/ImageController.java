package com.example.buenmate.Controller;


import org.springframework.security.access.prepost.PreAuthorize;
import com.example.buenmate.Service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/imagenes")
public class ImageController {

    @Autowired
    private ImageService imageService;

    // Recibe una imagen y devuelve la URL para acceder a ella
    // Solo los administradores pueden subir imágenes de productos.
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/subir")
    public ResponseEntity<Map<String, String>> subirImagen(@RequestParam("imagen") MultipartFile imagen) {
        String url = imageService.guardarImagen(imagen);
        return ResponseEntity.ok(Map.of("url", url));
    }


}
