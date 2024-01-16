package ru.hogwarts.school.services.impl.filestorage;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.services.impl.filestorage.exception.StorageFileNotFoundException;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Service
public class FileSystemStorageService {

    public void save(Path filePath, MultipartFile file) throws IOException {
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = file.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, StandardOpenOption.CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }
    }

    public Resource loadAsResource(Path file) {
        try {
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Не удалось прочитать файл: " + file.getFileName());
            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Не удалось прочитать файл: " + file.getFileName(), e);
        }
    }

}
