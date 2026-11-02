package com.example.buenmate.Service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String guardarImagen(MultipartFile file);
}