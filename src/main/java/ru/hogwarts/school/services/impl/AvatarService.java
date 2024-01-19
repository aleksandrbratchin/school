package ru.hogwarts.school.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.avatar.Avatar;
import ru.hogwarts.school.model.student.Student;
import ru.hogwarts.school.repositories.AvatarRepository;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.services.impl.filestorage.FileSystemStorageService;
import ru.hogwarts.school.specifications.AvatarSpecification;

import java.io.IOException;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


@Service
public class AvatarService {

    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;
    private final FileSystemStorageService fileSystemStorageService;

    public AvatarService(StudentRepository studentRepository, AvatarRepository avatarRepository, FileSystemStorageService fileSystemStorageService) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
        this.fileSystemStorageService = fileSystemStorageService;
    }

    public void uploadAvatar(UUID studentId, MultipartFile avatarFile) throws IOException {
        Student student = studentRepository.getReferenceById(studentId);
        Path filePath = Path.of(avatarsDir, studentId.toString(), Objects.requireNonNull(avatarFile.getOriginalFilename())).normalize().toAbsolutePath();
        fileSystemStorageService.deleteAll(filePath.getParent());
        fileSystemStorageService.save(filePath, avatarFile);
        Avatar avatar = avatarRepository.findOne(
                        AvatarSpecification.findByIdStudent(studentId)
                )
                .orElse(
                        Avatar.builder()
                                .student(student)
                                .build()
                );
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(avatarFile.getBytes());
        avatarRepository.save(avatar);
    }

    public Resource getAvatarFromFile(Avatar avatar) {
        Optional.ofNullable(avatar).orElseThrow(IllegalArgumentException::new);
        Path filePath = Path.of(avatar.getFilePath());
        return fileSystemStorageService.loadAsResource(filePath);
    }

    public Avatar findByStudentId(UUID studentId) {
        Optional.ofNullable(studentId).orElseThrow(IllegalArgumentException::new);
        return avatarRepository.findOne(AvatarSpecification.findByIdStudent(studentId))
                .orElseThrow(NoSuchElementException::new);
    }

    public Avatar findById(UUID id) {
        Optional.ofNullable(id).orElseThrow(IllegalArgumentException::new);
        return avatarRepository.findOne(AvatarSpecification.idEqual(id))
                .orElseThrow(NoSuchElementException::new);
    }
}
