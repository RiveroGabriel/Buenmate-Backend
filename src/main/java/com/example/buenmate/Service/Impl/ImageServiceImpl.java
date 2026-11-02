package com.example.buenmate.Service.Impl;

import com.example.buenmate.Service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    private final Path rootLocation = Paths.get("uploads");

    public ImageServiceImpl() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear la carpeta de imágenes", e);
        }
    }

    @Override
    public String guardarImagen(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("El archivo está vacío");
            }

            String contentType = file.getContentType();
            String extension = obtenerExtension(file.getOriginalFilename()).toLowerCase();

            boolean extensionValida = extension.equals(".jpg") ||
                    extension.equals(".jpeg") ||
                    extension.equals(".png") ||
                    extension.equals(".webp");

            if (contentType != null && !contentType.startsWith("image/")) {
                throw new RuntimeException("El archivo debe ser una imagen");
            }

            if (!extensionValida) {
                throw new RuntimeException("Solo se permiten imágenes .jpg, .jpeg, .png o .webp");
            }

            String nombreUnico = UUID.randomUUID().toString() + extension;
            Path destino = rootLocation.resolve(nombreUnico);
            Files.copy(file.getInputStream(), destino);

            return "/uploads/" + nombreUnico;

        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la imagen", e);
        }
    }

    private String obtenerExtension(String nombreOriginal) {
        if (nombreOriginal == null || !nombreOriginal.contains(".")) {
            return "";
        }
        return nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
    }
}