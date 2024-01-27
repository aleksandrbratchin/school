package ru.hogwarts.school.services.api;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;


public interface StorageService {

    void save(Path filePath, MultipartFile file);

    Resource loadAsResource(Path file);

    void deleteAll(Path location);

}
