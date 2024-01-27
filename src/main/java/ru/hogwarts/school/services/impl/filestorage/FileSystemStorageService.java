package ru.hogwarts.school.services.impl.filestorage;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.services.api.StorageService;
import ru.hogwarts.school.services.impl.filestorage.exception.StorageException;
import ru.hogwarts.school.services.impl.filestorage.exception.StorageFileNotFoundException;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class FileSystemStorageService implements StorageService {

    @Override
    public void save(Path filePath, MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Не удалось сохранить пустой файл.");
            }
            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);
            try (
                    InputStream is = file.getInputStream();
                    OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                    BufferedInputStream bis = new BufferedInputStream(is, 1024);
                    BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
            ) {
                bis.transferTo(bos);
            }
        } catch (IOException e) {
            throw new StorageException("Не удалось сохранить файл.", e);
        }
    }

    @Override
    public Resource loadAsResource(Path file) {
        try {
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException(
                        "Не удалось прочитать файл: " + file.getFileName());
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Не удалось прочитать файл: " + file.getFileName(), e);
        }
    }

    @Override
    public void deleteAll(Path location) {
        FileSystemUtils.deleteRecursively(location.toFile());
    }

}
